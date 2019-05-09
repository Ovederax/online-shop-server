package net.thumbtack.onlineshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.thumbtack.onlineshop.database.dao.BasketDao;
import net.thumbtack.onlineshop.database.dao.ProductDao;
import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.dto.request.basket.BuyProductFromBasketRequest;
import net.thumbtack.onlineshop.dto.request.basket.BasketUpdateCountProductRequest;
import net.thumbtack.onlineshop.dto.request.basket.AddProductToBasketsRequest;
import net.thumbtack.onlineshop.dto.response.basket.BuyProductFromBasketResponse;
import net.thumbtack.onlineshop.dto.response.basket.ProductInBasketResponse;
import net.thumbtack.onlineshop.dto.response.product.BuyProductResponse;
import net.thumbtack.onlineshop.model.entity.BasketItem;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.Product;
import net.thumbtack.onlineshop.model.entity.Purchase;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;
import net.thumbtack.onlineshop.model.transfer.PurchaseBuyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BasketService extends ServiceBase{
    private UserDao userDao;
    private BasketDao basketDao;
    private ProductDao productDao;

    @Autowired
    public BasketService(UserDao userDao, BasketDao basketDao, ProductDao productDao) {
        this.userDao = userDao;
        this.basketDao = basketDao;
        this.productDao = productDao;
    }

    public List<ProductInBasketResponse> addProductToBasket(AddProductToBasketsRequest dto, String token) throws JsonProcessingException, ServerException {
        Client client = getClientByToken(userDao, token);
        int count = dto.getCount();
        if(count == 0) {
            count = 1;
        }
        Product product = productDao.getProductById(dto.getId());
        checkProductDataAndThrow(product, dto.getName(), dto.getPrice());
        basketDao.addProductToBasket(client, new BasketItem(product, count));
        return getProductsInBasket(client);
    }

    public void deleteProductFromBasket(int id, String token) throws ServerException {
        getClientByToken(userDao, token);
        basketDao.deleteItemFromBasketById(id);
    }

    public List<ProductInBasketResponse> updateProductCount(BasketUpdateCountProductRequest dto, String token) throws ServerException {
        Client client = getClientByToken(userDao, token);

        BasketItem item = basketDao.getProductInBasket(dto.getId());
        checkProductDataAndThrow(item.getProduct(), dto.getName(), dto.getPrice());
        item.setCount(dto.getCount());
        basketDao.updateProductCount(item);
        return getProductsInBasket(client);
    }

    public List<ProductInBasketResponse> getProductsInBasket(String token) throws ServerException {
        Client client = getClientByToken(userDao, token);
        return getProductsInBasket(client);
    }


    private int choseBasketItemForBuy(List<BasketItem> basketItems, List<BuyProductFromBasketRequest> reqBasketItems, List<PurchaseBuyInfo> purchases) {
        int amount = 0;
        Iterator<BasketItem> itemIterator = basketItems.iterator();
        while ( itemIterator.hasNext() ) {
            BasketItem item = itemIterator.next();
            for (BuyProductFromBasketRequest reqItem : reqBasketItems) {
                if (item.getId() == reqItem.getId()) {
                    boolean valid = checkProductData(item.getProduct(), reqItem.getName(), reqItem.getPrice());
                    if(item.getProduct().getIsDeleted() > 0) {
                        valid = false;
                    }
                    if (valid) {
                        int count;
                        if(reqItem.getCount() == null || reqItem.getCount() > item.getCount()) {
                            count = item.getCount();
                        } else {
                            count = reqItem.getCount();
                        }
                        int newCount = item.getProduct().getCounter() - count;
                        if( newCount < 0 ) {
                            valid = false;
                        }
                        if( valid ) {
                            amount += count * item.getProduct().getPrice();
                            Purchase purchase = new Purchase(item.getProduct(), item.getProduct().getName(), count, item.getProduct().getPrice());
                            purchases.add(new PurchaseBuyInfo(purchase, item, newCount));
                        }
                    }
                    break;
                }
            }
        }
        return amount;
    }

    public BuyProductFromBasketResponse buyProductFromBasket(List<BuyProductFromBasketRequest> dto, String token) throws ServerException {
//        1)   Формируем лист покупок отсеевая лишнее
//        1.5) Если у клиента нет достаточных средств на эти покупки, то заканчиваем --->
//        2)   Поочередно отправляем элементы списка в метод покупки, успешно/неуспешно купленное добовляем в различные листы
//        2.1) В случае возникновения исключения при любой покупке из списка - отмена транзакции
        Client client = getClientByToken(userDao, token);
        List<Integer> basketItemsId = dto.stream().map(BuyProductFromBasketRequest::getId).collect(Collectors.toList());

        List<BasketItem> basketItems = basketDao.getProductInBasketInRange(client, basketItemsId);
        List<PurchaseBuyInfo>   purchaseBuyInfo = new ArrayList<>();
        int amount = choseBasketItemForBuy(basketItems, dto, purchaseBuyInfo);

        if(amount > client.getMoney()) {
            throw new ServerException(ErrorCode.YOU_NEED_MORE_MONEY_TO_BUY);
        }
        int newDeposit = client.getMoney() - amount;

        productDao.buyProductsFromBasket(purchaseBuyInfo, client, newDeposit);

        List<BuyProductResponse> successList = purchaseBuyInfo.stream()
                .map((buyInfo)->{
                    Purchase purchase = buyInfo.getPurchase();
                    Product product = purchase.getActual();
                    return new BuyProductResponse(purchase.getId(), product.getName(), product.getPrice(), purchase.getBuyCount());
                }).collect(Collectors.toList());

        List<ProductInBasketResponse> remainingList = basketDao.getProductsInBasket(client).stream()
            .map((basketItem)-> {
                Product product = basketItem.getProduct();
                return new ProductInBasketResponse(basketItem.getId(), product.getName(), product.getPrice(), basketItem.getCount());
            }).collect(Collectors.toList());

        return new BuyProductFromBasketResponse(successList, remainingList);
    }

    private boolean checkProductData(Product p, String name, int price) {
        return p.getName().equals(name) && p.getPrice() == price;
    }

    private void checkProductDataAndThrow(Product p, String name, int price) throws ServerException {
        ErrorCode code = ErrorCode.BAD_UPDATE_PRODUCT_IN_BASKET;
        if(!(p.getName().equals(name))) {
            code.setField("name");
            throw new ServerException(code);
        }
        if(!(p.getPrice() == price)) {
            code.setField("price");
            throw new ServerException(code);
        }
    }

    private List<ProductInBasketResponse> getProductsInBasket(Client client) throws ServerException {
        List<BasketItem> p = basketDao.getProductsInBasket(client);
        List<ProductInBasketResponse> list = new ArrayList<>(p.size());
        for(BasketItem it : p) {
            Product product = it.getProduct();
            list.add(new ProductInBasketResponse(it.getId(), product.getName(), product.getPrice(), it.getCount()));
        }
        return list;
    }
}
