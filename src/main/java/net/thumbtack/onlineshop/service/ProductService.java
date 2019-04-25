package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.database.dao.CategoryDao;
import net.thumbtack.onlineshop.database.dao.ProductDao;
import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.dto.request.product.ProductAddRequest;
import net.thumbtack.onlineshop.dto.request.product.ProductBuyRequest;
import net.thumbtack.onlineshop.dto.request.product.ProductEditRequest;
import net.thumbtack.onlineshop.dto.response.product.GetProductResponse;
import net.thumbtack.onlineshop.dto.response.product.ProductBuyResponse;
import net.thumbtack.onlineshop.dto.response.product.ProductResponse;
import net.thumbtack.onlineshop.enums.ProductSortOrder;
import net.thumbtack.onlineshop.model.entity.*;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProductService extends ServiceBase{
    private ProductDao productDao;
    private UserDao userDao;
    private CategoryDao categoryDao;

    @Autowired
    public ProductService(ProductDao productDao, UserDao userDao, CategoryDao categoryDao) {
        this.productDao = productDao;
        this.userDao = userDao;
        this.categoryDao = categoryDao;
    }

    private List<String> getCategoriesListNames(Product p) {
        List<String> list = new ArrayList<>();
        for(Category it : p.getCategories()) {
            list.add(it.getName());
        }
        return list;
    }

    private List<Integer> getCategoriesListId(List<Category> categories) {
        List<Integer> list = new ArrayList<>();
        if(categories == null) {
            return list;
        }
        for(Category it : categories) {
            list.add(it.getId());
        }
        return list;
    }

    public ProductResponse addProduct(ProductAddRequest dto, String token) throws ServerException {
        checkAdministratorPrivileges(userDao, token);
        int id = productDao.addProduct(new Product(dto.getName(), dto.getPrice(), dto.getCount(), null), dto.getCategories());
        return new ProductResponse(id, dto.getName(), dto.getPrice(), dto.getCount(), dto.getCategories() != null ? dto.getCategories() : new ArrayList<>());
    }

    public ProductResponse updateProduct(int id, ProductEditRequest dto, String token) throws ServerException {
        checkAdministratorPrivileges(userDao, token);
        Product product = productDao.findProductById(id);
        List<Category> categories = null;
        if(dto.getCategories() != null) {
            categories = categoryDao.findCategoriesById(dto.getCategories());
        }
        productDao.updateProduct(product, dto.getName(), dto.getPrice(), dto.getCount(), categories);
        if(dto.getCategories() == null) {
            return new ProductResponse(id, dto.getName(), dto.getPrice(), dto.getCount(), getCategoriesListId(product.getCategories()));
        }
        return new ProductResponse(id, dto.getName(), dto.getPrice(), dto.getCount(), getCategoriesListId(categories));
    }

    public void deleteProduct(int id, String token) throws ServerException {
        checkAdministratorPrivileges(userDao, token);
        // TODO
        // Вернутся после завершения Корзины и Покупок
        // Не ясно как должна работать логика удаления продукта
        // Логика зависит от того как это будет работать в совокупности с корзиной и покупками
        // Пока оставлю такую реализацию
        productDao.markProductAsDeleted(productDao.findProductById(id));
    }

    public GetProductResponse getProduct(int id, String token) throws ServerException {
        User u = userDao.findUserByToken(token); // check user exist
        Product p = productDao.findProductById(id);
        return new GetProductResponse(p.getId(), p.getName(), p.getPrice(), p.getCounter(), getCategoriesListNames(p));
    }

    private List<GetProductResponse> getProductListWithoutCategory() {
        List<GetProductResponse> list = new ArrayList<>();
        List<Product> products = productDao.getProductListOrderProductNoCategory();
        for(Product it : products) {
            list.add(new GetProductResponse(it.getId(), it.getName(), it.getPrice(), it.getCounter(), new ArrayList<>()));
        }
        return list;
    }
    public List<GetProductResponse> getProductsList(List<Integer> categoriesId, String order, String token) throws ServerException, IOException {
        userDao.findUserByToken(token); // check user exist

        if (categoriesId != null && categoriesId.size() == 0) {
            return getProductListWithoutCategory();
        }

        ProductSortOrder sortOrder;

        if(order == null) {
        	sortOrder = ProductSortOrder.PRODUCT;
        } else  {
            sortOrder = ProductSortOrder.fromString(order);
        }

        if (sortOrder == ProductSortOrder.PRODUCT) {
            List<GetProductResponse> list = new ArrayList<>();
            List<Product> products = productDao.getProductListOrderProduct(categoriesId);
            for (Product it : products) {
                list.add(new GetProductResponse(it.getId(), it.getName(), it.getPrice(), it.getCounter(), getCategoriesListNames(it)));
            }
            return list;
        } else if(sortOrder == ProductSortOrder.CATEGORY) {
            List<GetProductResponse> list;
            if (categoriesId == null) { // так надо
                list = getProductListWithoutCategory();
            } else {
                list = new ArrayList<>();
            }

            List<Category> categories = productDao.getProductListOrderCategory(categoriesId);
            for (Category category : categories) {
                for (Product it : category.getProducts()) {
                    list.add(new GetProductResponse(it.getId(), it.getName(), it.getPrice(), it.getCounter(), Collections.singletonList(category.getName())));
                }
            }
            return list;
        } else {
            throw new ServerException(ErrorCode.BAD_ORDER_FOR_GET_PROGUCT_LIST);
        }
    }

    private void checkProductParameters(Product product, String name, int price, int count) throws ServerException {
        ErrorCode error = ErrorCode.NO_BUY_IF_PRODUCT_IS_CHANGE;
        if(!product.getName().equals(name)) {
            error.setField("name");
            throw new ServerException(error);
        }
        if(product.getPrice() != price) {
            error.setField("price");
            throw new ServerException(error);
        }
        if(product.getCounter() < count) {
            error.setField("count");
            throw new ServerException(ErrorCode.BUY_COUNT_PRODUCT_LESS_NEED_COUNT);
        }
    }

    public ProductBuyResponse buyProduct(ProductBuyRequest dto, String token) throws ServerException {
        Client client = getClientByToken(userDao, token);
        Product product = productDao.findProductById(dto.getId());
        int count = 1;
        if(dto.getCount() != null) {
            count = dto.getCount();
        }
        int totalCost = count*dto.getPrice();
        if(client.getDeposit().getMoney() < totalCost) {
            throw new ServerException(ErrorCode.YOU_NEED_MORE_MONEY_TO_BUY);
        }
        int newMoneyDeposit = client.getDeposit().getMoney() - totalCost;
        int newProductCount = product.getCounter() - count;
        checkProductParameters(product, dto.getName(), dto.getPrice(), count);
        int id = productDao.buyProduct(new Purchase(product, dto.getName(), count, product.getPrice()), client, newMoneyDeposit, newProductCount);
        return new ProductBuyResponse(id, dto.getName(), dto.getPrice(), count);
    }
}
