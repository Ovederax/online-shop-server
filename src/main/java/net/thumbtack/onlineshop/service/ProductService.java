package net.thumbtack.onlineshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import net.thumbtack.onlineshop.database.dao.CategoryDao;
import net.thumbtack.onlineshop.database.dao.ProductDao;
import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.database.mybatis.transfer.ProductDTO;
import net.thumbtack.onlineshop.dto.request.product.ProductAddRequest;
import net.thumbtack.onlineshop.dto.request.product.ProductBuyRequest;
import net.thumbtack.onlineshop.dto.request.product.ProductEditRequest;
import net.thumbtack.onlineshop.dto.response.product.ProductGetResponse;
import net.thumbtack.onlineshop.dto.response.product.ProductResponse;
import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.Product;
import net.thumbtack.onlineshop.model.exeptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private @Autowired ObjectMapper mapper;
    private ProductDao productDao;
    private UserService userService;

    @Autowired
    public ProductService(ProductDao productDao, UserService userService) {
        this.productDao = productDao;
        this.userService = userService;
    }

    private List<String> getNamesCategories(Product p) {
        List<String> list = new ArrayList<>();
        for(Category it : p.getCategories()) {
            list.add(it.getName());
        }
        return list;
    }

    public String addProduct(ProductAddRequest dto, String token) throws UserException, JsonProcessingException {
        userService.checkAdministratorPrivileges(token);
        int id = productDao.addProduct(new Product(dto.getName(), dto.getPrice(), dto.getCount(), null), dto.getCategories());
        Product p = productDao.findProductById(id);

        List<Integer> list = new ArrayList<>();
        for(Category it : p.getCategories()) {
            list.add(it.getId());
        }
        return mapper.writeValueAsString(new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getCount(), list));
    }

    public String updateProduct(int id, ProductEditRequest dto, String token) throws UserException, JsonProcessingException {
        userService.checkAdministratorPrivileges(token);
        productDao.updateProduct(new ProductDTO(id, dto.getName(), dto.getPrice(), dto.getCount(), dto.getCategories()));
        Product p = productDao.findProductById(id);

        List<Integer> list = new ArrayList<>();
        for(Category it : p.getCategories()) {
            list.add(it.getId());
        }
        return mapper.writeValueAsString(new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getCount(), list));
    }

    public void deleteProduct(int id, String token) throws UserException {
        userService.checkAdministratorPrivileges(token);
        productDao.deleteProductById(id);
    }

    public String getProduct(int id, String token) throws UserException, JsonProcessingException {
        userService.checkUserExistByToken(token);
        Product p = productDao.findProductById(id);
        return mapper.writeValueAsString(new ProductGetResponse(p.getId(), p.getName(), p.getPrice(),
                p.getCount(), getNamesCategories(p)));
    }

    public String getProductsList(String category, String order, String token) throws UserException, IOException {
        userService.checkUserExistByToken(token);
        List<Integer> categoriesId = null;
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
        return mapper.writeValueAsString(list);
    }

    public String buyProduct(ProductBuyRequest dto, String token) throws UserException, JsonProcessingException {
        Client c = userService.getClientByToken(token);
        List<Product> list = productDao.buyProduct(c.getId(), new ProductDTO(dto.getId(), dto.getName(), dto.getPrice(), dto.getCount(), null));
        List<ProductBuyRequest> out = new ArrayList<>();
        for(Product it : list) {
            out.add(new ProductBuyRequest(it.getId(), it.getName(), it.getPrice(), it.getCount()));
        }
        return mapper.writeValueAsString(out);
    }
}
