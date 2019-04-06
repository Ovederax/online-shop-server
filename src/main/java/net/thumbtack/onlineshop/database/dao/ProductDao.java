package net.thumbtack.onlineshop.database.dao;

import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.Product;
import java.util.List;

public interface ProductDao {
    int addProduct(Product product, List<Integer> categories);
    Product findProductById(int id);
    void updateProduct(Product product, List<Integer> categories);
    void deleteProductById(int id);
    List<Product> getProductsList(List<Integer> categoriesId, String order);
    List<Product> buyProduct(Client userId, Product product, Integer count);
}
