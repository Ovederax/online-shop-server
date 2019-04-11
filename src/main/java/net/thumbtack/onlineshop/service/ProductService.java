package net.thumbtack.onlineshop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.database.dao.ProductDao;
import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.dto.request.product.ProductAddRequest;
import net.thumbtack.onlineshop.dto.request.product.ProductBuyRequest;
import net.thumbtack.onlineshop.dto.request.product.ProductEditRequest;
import net.thumbtack.onlineshop.dto.response.product.ProductGetResponse;
import net.thumbtack.onlineshop.dto.response.product.ProductResponse;
import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.Product;
import net.thumbtack.onlineshop.model.entity.User;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ProductService {
    private ProductDao productDao;
    private UserDao userDao;
    private UserService userService;
    private @Autowired ObjectMapper mapper;

    @Autowired
    public ProductService(ProductDao productDao, UserDao userDao, UserService userService) {
        this.productDao = productDao;
        this.userDao = userDao;
        this.userService = userService;
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
        for(Category it : categories) {
            list.add(it.getId());
        }
        return list;
    }

    public ProductResponse addProduct(ProductAddRequest dto, String token) throws ServerException {
        userService.checkAdministratorPrivileges(token);
        int id = productDao.addProduct(new Product(dto.getName(), dto.getPrice(), dto.getCount(), null), dto.getCategories());
        Product p = productDao.findProductById(id);
        return new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getCounter(), getCategoriesListId(p.getCategories()));
    }

    public ProductResponse updateProduct(int id, ProductEditRequest dto, String token) throws ServerException {
        userService.checkAdministratorPrivileges(token);
        Product product = productDao.findProductById(id);
        // возможно стоило сделать запрос в categoryDao на список категорий, но это будет бесполезной тратой ресурсов
        productDao.updateProduct(product, dto.getName(), dto.getPrice(), dto.getCount(), dto.getCategories());
        Product p = productDao.findProductById(id);
        return new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getCounter(), getCategoriesListId(p.getCategories()));
    }

    public void deleteProduct(int id, String token) throws ServerException {
        userService.checkAdministratorPrivileges(token);
        // TODO
        // Вернутся после завершения Корзины и Покупок
        // Не ясно как должна работать логика удаления продукта
        // Логика зависит от того как это будет работать в совокупности с корзиной и покупками
        // Пока оставлю такую реализацию
        productDao.markProductAsDeleted(productDao.findProductById(id));
    }

    public ProductGetResponse getProduct(int id, String token) throws ServerException {
        User u = userDao.findUserByToken(token); // check user exist
        Product p = productDao.findProductById(id);
        return new ProductGetResponse(p.getId(), p.getName(), p.getPrice(), p.getCounter(), getCategoriesListNames(p));
    }

    private List<ProductGetResponse> getProductListWithoutCategory() {
        List<ProductGetResponse> list = new ArrayList<>();
        List<Product> products = productDao.getProductListOrderProductNoCategory();
        for(Product it : products) {
            list.add(new ProductGetResponse(it.getId(), it.getName(), it.getPrice(), it.getCounter(), new ArrayList<>()));
        }
        return list;
    }
    public List<ProductGetResponse> getProductsList(List<Integer> categoriesId, String order, String token) throws ServerException, IOException {
        User u = userDao.findUserByToken(token); // check user exist

        if (categoriesId != null && categoriesId.size() == 0) {
            return getProductListWithoutCategory();
        }

        if(order == null) {
            order = "product";
        }

        if (order.equals("product")) {
            List<ProductGetResponse> list = new ArrayList<>();
            List<Product> products = productDao.getProductListOrderProduct(categoriesId);
            for (Product it : products) {
                list.add(new ProductGetResponse(it.getId(), it.getName(), it.getPrice(), it.getCounter(), getCategoriesListNames(it)));
            }
            return list;
        } else if(order.equals("category")) {
            List<ProductGetResponse> list;
            if (categoriesId == null) { // так надо
                list = getProductListWithoutCategory();
            } else {
                list = new ArrayList<>();
            }

            List<Category> categories = productDao.getProductListOrderCategory(categoriesId);
            for (Category category : categories) {
                for (Product it : category.getProducts()) {
                    list.add(new ProductGetResponse(it.getId(), it.getName(), it.getPrice(), it.getCounter(), Collections.singletonList(category.getName())));
                }
            }
            return list;
        } else {
            throw new ServerException(ErrorCode.BAD_ORDER_FOR_GET_PROGUCT_LIST);
        }
    }

    public List<ProductBuyRequest> buyProduct(ProductBuyRequest dto, String token) throws ServerException {
        Client client = userService.getClientByToken(token);
        List<Product> list = productDao.buyProduct(client, new Product(dto.getId(), dto.getName(), dto.getPrice(), 0, null), dto.getCount());
        List<ProductBuyRequest> out = new ArrayList<>();
        for(Product it : list) {
            out.add(new ProductBuyRequest(it.getId(), it.getName(), it.getPrice(), it.getCounter()));
        }
        return out;
    }
}
