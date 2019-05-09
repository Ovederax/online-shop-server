package net.thumbtack.onlineshop.database.dao;

import net.thumbtack.onlineshop.model.entity.BasketItem;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.exeptions.ServerException;

import java.util.List;

public interface BasketDao {
	int addProductToBasket(Client client, BasketItem item) throws ServerException;
    List<BasketItem> getProductsInBasket(Client client) throws ServerException;
    void updateProductCount(BasketItem product) throws ServerException;

    BasketItem getProductInBasket(int id) throws ServerException;

    void deleteItemFromBasketById(int id) throws ServerException;
    List<BasketItem> getProductInBasketInRange(Client client, List<Integer> productsId) throws ServerException;
}
