package net.thumbtack.onlineshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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

    private List<String> getNamesCategories(Product p) {
        List<String> list = new ArrayList<>();
        for(Category it : p.getCategories()) {
            list.add(it.getName());
        }
        return list;
    }

    public ProductResponse addProduct(ProductAddRequest dto, String token) throws ServerException, JsonProcessingException {
        userService.checkAdministratorPrivileges(token);
        int id = productDao.addProduct(new Product(dto.getName(), dto.getPrice(), dto.getCount(), null), dto.getCategories());
        Product p = productDao.findProductById(id);

        List<Integer> list = new ArrayList<>();
        for(Category it : p.getCategories()) {
            list.add(it.getId());
        }
        return new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getCount(), list);
    }

    public ProductResponse updateProduct(int id, ProductEditRequest dto, String token) throws ServerException, JsonProcessingException {
        userService.checkAdministratorPrivileges(token);
        Product product = productDao.findProductById(id);
        // возможно стоило сделать запрос в categoryDao на список категорий, но это будет бесполезной тратой ресурсов
        product.updateEntity(dto.getName(), dto.getPrice(), dto.getCount());
        productDao.updateProduct(product, dto.getCategories());

        List<Integer> list = new ArrayList<>();
        for(Category it : product.getCategories()) {
            list.add(it.getId());
        }
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount(), list);
    }

    public void deleteProduct(int id, String token) throws ServerException {
        userService.checkAdministratorPrivileges(token);
        productDao.deleteProductById(id);
    }

    public ProductGetResponse getProduct(int id, String token) throws ServerException, JsonProcessingException {
        User u = userDao.findUserByToken(token); // check user exist
        Product p = productDao.findProductById(id);
        return new ProductGetResponse(p.getId(), p.getName(), p.getPrice(), p.getCount(), getNamesCategories(p));
    }

    public List<ProductGetResponse> getProductsList(String category, String order, String token) throws ServerException, IOException {
        User u = userDao.findUserByToken(token); // check user exist
        List<Integer> categoriesId;
        if(category == null || category.equals("")) {
            categoriesId = null;
        } else {
            categoriesId = mapper.readValue(category, new TypeReference<List<Integer>>(){});
        }

        List<Product> products = productDao.getProductsList(categoriesId, order);

        List<ProductGetResponse> list = new ArrayList<>();
        for(Product it : products) {
            list.add(new ProductGetResponse(it.getId(), it.getName(), it.getPrice(), it.getCount(), getNamesCategories(it)));
        }
        return list;
    }

    public List<ProductBuyRequest> buyProduct(ProductBuyRequest dto, String token) throws ServerException, JsonProcessingException {
        Client client = userService.getClientByToken(token);
        List<Product> list = productDao.buyProduct(client, new Product(dto.getId(), dto.getName(), dto.getPrice(), 0, null), dto.getCount());
        List<ProductBuyRequest> out = new ArrayList<>();
        for(Product it : list) {
            out.add(new ProductBuyRequest(it.getId(), it.getName(), it.getPrice(), it.getCount()));
        }
        return out;
    }
}
