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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    private void checkProductData(Product p, String name, int price) throws ServerException {
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

    public List<ProductInBasketResponse> addProductToBasket(AddProductToBasketsRequest dto, String token) throws JsonProcessingException, ServerException {
        Client client = getClientByToken(userDao, token);
        int count = dto.getCount();
        if(count == 0) {
            count = 1;
        }
        Product product = productDao.getProductById(dto.getId());
        checkProductData(product, dto.getName(), dto.getPrice());
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
        checkProductData(item.getProduct(), dto.getName(), dto.getPrice());
        item.setCount(dto.getCount());
        basketDao.updateProductCount(item);
        return getProductsInBasket(client);
    }

    private List<ProductInBasketResponse> getProductsInBasket(Client client) {
        List<BasketItem> p = basketDao.getProductsInBasket(client);
        List<ProductInBasketResponse> list = new ArrayList<>(p.size());
        for(BasketItem it : p) {
            Product product = it.getProduct();
            list.add(new ProductInBasketResponse(it.getId(), product.getName(), product.getPrice(), it.getCount()));
        }
        return list;
    }

    public List<ProductInBasketResponse> getProductsInBasket(String token) throws ServerException {
        Client client = getClientByToken(userDao, token);
        return getProductsInBasket(client);
    }

    public BuyProductFromBasketResponse buyProductFromBasket(List<BuyProductFromBasketRequest> dto, String token) throws ServerException {
//        1)   Формируем лист покупок отсеевая лишнее
//        1.5) Если у клиента нет достаточных средств на эти покупки, то заканчиваем --->
//        2)   Поочередно отправляем элементы списка в метод покупки, успешно/неуспешно купленное добовляем в различные листы

        Client client = getClientByToken(userDao, token);

        List<Integer> productsId = new ArrayList<>();
        for(BuyProductFromBasketRequest it : dto) {
            productsId.add(it.getId());
        }
        List<BasketItem> basketItems = basketDao.getProductInBasketInRange(client, productsId);

        List<Purchase> purchases = new ArrayList<>();

        int amount = 0;
        // REVU for (BasketItem item : basketItems)
        for(int i=0; i<basketItems.size(); ++i) {
            Product product = basketItems.get(i).getProduct();
            if(product.getIsDeleted() == 1) {
                continue;
            }
            BuyProductFromBasketRequest it = dto.get(i);
            int count;
            if(it.getCount() == 0 || it.getCount() > basketItems.get(i).getCount()) {
                count = basketItems.get(i).getCount();
            } else {
                count = it.getCount();
            }
            Purchase purchase = new Purchase(product, it.getName(), count, it.getPrice());
            amount += it.getCount() * it.getPrice();
            purchases.add(purchase);
        }
        if(amount > client.getMoney()) {
            throw new ServerException(ErrorCode.YOU_NEED_MORE_MONEY_TO_BUY);
        }

        List<BuyProductResponse> successList = new ArrayList<>();
        // REVU you buy every product independently
        // but you must do a transaction for all list
        for (Purchase it : purchases) {
            Product product = it.getActual();
            try {
                int newDeposit = client.getMoney() - it.getBuyCount() * it.getBuyPrice();
                int newCount = product.getCounter() - it.getBuyCount();
                // REVU you buy every product independently
                // but you must do a transaction for all list
                productDao.buyProductFromBasket(it, client, newDeposit, newCount);
                successList.add(new BuyProductResponse(product.getId(), product.getName(), product.getPrice(), it.getBuyCount()));
            } catch (ServerException ex) {
                if (ex.getErrorCode().equals(ErrorCode.NO_BUY_IF_CLIENT_DEPOSIT_IS_CHANGE.getErrorCode())) {
                    break;
                } /* else if product is change it's normally*/
            }
        }
        List<ProductInBasketResponse> remainingList = new ArrayList<>();
        for(BasketItem it : basketDao.getProductsInBasket(client)) {
            Product product = it.getProduct();
            remainingList.add(new ProductInBasketResponse(it.getId(), product.getName(), product.getPrice(), it.getCount()));
        }
        return new BuyProductFromBasketResponse(successList, remainingList);
    }
}
