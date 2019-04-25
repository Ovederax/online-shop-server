package net.thumbtack.onlineshop.database.dao;

import net.thumbtack.onlineshop.model.entity.BasketItem;
import net.thumbtack.onlineshop.model.entity.Client;

import java.util.List;

public interface BasketDao {
	int addProductToBasket(Client client, BasketItem item);
    List<BasketItem> getProductsInBasket(Client client);
    void updateProductCount(BasketItem product);

    BasketItem getProductInBasket(int id);

    void deleteItemFromBasketById(int id);
    void deleteItemFromBasketByProductId(int id);

    List<BasketItem> getProductInBasketInRange(Client client, List<Integer> productsId);
}
