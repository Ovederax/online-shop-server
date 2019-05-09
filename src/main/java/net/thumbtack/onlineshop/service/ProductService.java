package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.database.dao.CategoryDao;
import net.thumbtack.onlineshop.database.dao.ProductDao;
import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.dto.request.product.AddProductRequest;
import net.thumbtack.onlineshop.dto.request.product.BuyProductRequest;
import net.thumbtack.onlineshop.dto.request.product.EditProductRequest;
import net.thumbtack.onlineshop.dto.response.product.GetProductResponse;
import net.thumbtack.onlineshop.dto.response.product.BuyProductResponse;
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
import java.util.stream.Collectors;

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

    public ProductResponse addProduct(AddProductRequest dto, String token) throws ServerException {
        checkAdministratorPrivileges(userDao, token);
        int id = productDao.addProduct(new Product(dto.getName(), dto.getPrice(), dto.getCount(), null), dto.getCategories());
        return new ProductResponse(id, dto.getName(), dto.getPrice(), dto.getCount(), dto.getCategories() != null ? dto.getCategories() : new ArrayList<>());
    }

    public ProductResponse updateProduct(int id, EditProductRequest dto, String token) throws ServerException {
        checkAdministratorPrivileges(userDao, token);
        Product product = productDao.getProductById(id);
        List<Category> categories = null;
        if(dto.getCategories() != null) {
            categories = categoryDao.getCategoriesById(dto.getCategories());
        }
        productDao.updateProduct(product, dto.getName(), dto.getPrice(), dto.getCount(), categories);
        if(dto.getCategories() == null) {
            return new ProductResponse(id, dto.getName(), dto.getPrice(), dto.getCount(), getCategoriesListId(product.getCategories()));
        }
        return new ProductResponse(id, dto.getName(), dto.getPrice(), dto.getCount(), getCategoriesListId(categories));
    }

    public void deleteProduct(int id, String token) throws ServerException {
        checkAdministratorPrivileges(userDao, token);
        productDao.markProductAsDeleted(productDao.getProductById(id));
    }

    public GetProductResponse getProduct(int id, String token) throws ServerException {
    	userDao.getUserByToken(token);
        Product product = productDao.getProductById(id);
        return new GetProductResponse(product.getId(), product.getName(),
                product.getPrice(), product.getCounter(), getCategoriesListNames(product));
    }

    public List<GetProductResponse> getProductsList(List<Integer> categoriesId, String order, String token) throws ServerException, IOException {
        userDao.getUserByToken(token); // check user exist

        if (categoriesId != null && categoriesId.size() == 0) {
            return getProductListWithoutCategory();
        }

        ProductSortOrder sortOrder;

        if(order == null) {
        	sortOrder = ProductSortOrder.PRODUCT;
        } else  {
            sortOrder = ProductSortOrder.fromString(order);
        }

        switch (sortOrder) {
            case PRODUCT: {
                List<GetProductResponse> list = new ArrayList<>();
                List<Product> products = productDao.getProductListOrderProduct(categoriesId);
                for (Product it : products) {
                    list.add(new GetProductResponse(it.getId(), it.getName(), it.getPrice(), it.getCounter(), getCategoriesListNames(it)));
                }
                return list;
            }
            case CATEGORY: {
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
            }
            default:
                throw new ServerException(ErrorCode.BAD_ORDER_FOR_GET_PRODUCT_LIST);
        }
    }
    public BuyProductResponse buyProduct(BuyProductRequest dto, String token) throws ServerException {
        Client client = getClientByToken(userDao, token);
        Product product = productDao.getProductById(dto.getId());
        int count = 1;
        if(dto.getCount() != null) {
            count = dto.getCount();
        }
        int totalCost = count*dto.getPrice();
        if(client.getMoney() < totalCost) {
            throw new ServerException(ErrorCode.YOU_NEED_MORE_MONEY_TO_BUY);
        }

        int newMoneyDeposit = client.getMoney() - totalCost;
        int newProductCount = product.getCounter() - count;
        checkProductParameters(product, dto.getName(), dto.getPrice(), count);
        int id = productDao.buyProduct(new Purchase(product, dto.getName(), count, product.getPrice()), client, newMoneyDeposit, newProductCount);
        return new BuyProductResponse(id, dto.getName(), dto.getPrice(), count);
    }

    private void checkProductParameters(Product product, String name, int price, int count) throws ServerException {
        List<ErrorCode> errorCodes = new ArrayList<>();
        if(!product.getName().equals(name)) {
            errorCodes.add(ErrorCode.NO_BUY_PRODUCT_IF_NAME_IS_CHANGE);
        }
        if(product.getPrice() != price) {
            errorCodes.add(ErrorCode.NO_BUY_PRODUCT_IF_PRICE_IS_CHANGE);
        }
        if(product.getCounter() < count) {
            errorCodes.add(ErrorCode.NO_BUY_PRODUCT_IF_COUNT_IS_CHANGE);
        }
        if(errorCodes.size() != 0) {
            throw ServerException.instanceFromErrorCodeList(errorCodes);
        }
    }

    private List<Integer> getCategoriesListId(List<Category> categories) {
        if(categories == null) {
            return new ArrayList<>();
        }
        return categories.stream().map(Category::getId).collect(Collectors.toList());
    }

    private List<GetProductResponse> getProductListWithoutCategory() throws ServerException {
        List<GetProductResponse> list = new ArrayList<>();
        List<Product> products = productDao.getProductListOrderProductNoCategory();
        for(Product it : products) {
            list.add(new GetProductResponse(it.getId(), it.getName(), it.getPrice(), it.getCounter(), new ArrayList<>()));
        }
        return list;
    }
}
