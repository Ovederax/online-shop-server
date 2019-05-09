package net.thumbtack.onlineshop.database.dao;

import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.exeptions.ServerException;

import java.util.List;

public interface CategoryDao {
    void addCategory(Category category) throws ServerException;
    Category getCategory(int id) throws ServerException;
    void updateCategory (int id, String name, Integer parentId) throws ServerException;
    void deleteCategory (int id) throws ServerException;
    List<Category> getParentsCategories() throws ServerException;
    Category getCategoryById(int id) throws ServerException;

    List<Category> getCategoriesById(List<Integer> categories) throws ServerException;
}
