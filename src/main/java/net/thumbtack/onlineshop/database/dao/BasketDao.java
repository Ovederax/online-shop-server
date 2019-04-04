package net.thumbtack.onlineshop.database.dao;

import net.thumbtack.onlineshop.database.mybatis.transfer.ProductDTO;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.Product;

import java.util.List;

public interface BasketDao {
    void addProductInBasket(Client client, ProductDTO productDTO);
    List<Product> getProductsInBasket();
    void deleteProductFromBasket(int id, Client client);
    void updateProductCount(ProductDTO dto, Client client);
}
