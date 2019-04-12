package net.thumbtack.onlineshop.database.dao;

import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.Product;

import java.util.List;

public interface BasketDao {
	// REVU addProductToBasket
	void addProductInBasket(Client client, Product product, int count);
    List<Product> getProductsInBasket();
    void deleteProductFromBasket(int id, Client client);
    void updateProductCount(Product product, Client client);
}
