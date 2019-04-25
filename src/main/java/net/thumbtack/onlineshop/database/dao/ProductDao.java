package net.thumbtack.onlineshop.database.dao;

import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.Product;
import net.thumbtack.onlineshop.model.entity.Purchase;
import net.thumbtack.onlineshop.model.exeptions.ServerException;

import java.util.List;

public interface ProductDao {
    int addProduct(Product product, List<Integer> categories);
    Product findProductById(int id);
    void updateProduct(Product product, String name, Integer price, Integer counter, List<Category> categories) throws ServerException;
    void markProductAsDeleted(Product id);
    int buyProduct(Purchase purchase, Client client, int newMoneyDeposit, int newProductCount) throws ServerException;
    List<Product> getProductListOrderProduct(List<Integer> categoriesId);
    List<Product> getProductListOrderProductNoCategory();
    List<Category> getProductListOrderCategory(List<Integer> categoriesId);

}
