package net.thumbtack.onlineshop.database.dao;

import net.thumbtack.onlineshop.database.mybatis.transfer.ProductDTO;
import net.thumbtack.onlineshop.model.entity.Product;

import java.util.List;

public interface ProductDao {
    int addProduct(Product product, List<Integer> categories);
    Product findProductById(int id);
    void updateProduct(ProductDTO dto);
    void deleteProductById(int id);
    List<Product> getProductsList(List<Integer> categoriesId, String order);

    List<Product> buyProduct(int userId, ProductDTO productDTO);
}
