package net.thumbtack.onlineshop.database.dao;

import net.thumbtack.onlineshop.model.entity.*;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.transfer.PurchaseBuyInfo;

import java.util.List;

public interface ProductDao {
    int addProduct(Product product, List<Integer> categories) throws ServerException;
    Product getProductById(int id) throws ServerException;
    void updateProduct(Product product, String name, Integer price, Integer counter, List<Category> categories) throws ServerException;
    void markProductAsDeleted(Product id) throws ServerException;
    int buyProduct(Purchase purchase, Client client, int newMoneyDeposit, int newProductCount) throws ServerException;
    void buyProductsFromBasket(List<PurchaseBuyInfo> purchases, Client client, int newDeposit) throws ServerException;
    List<Product> getProductListOrderProduct(List<Integer> categoriesId) throws ServerException;
    List<Product> getProductListOrderProductNoCategory() throws ServerException;
    List<Category> getProductListOrderCategory(List<Integer> categoriesId) throws ServerException;
    List<Product> getAllProduct() throws ServerException;

}
