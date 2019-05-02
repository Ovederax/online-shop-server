package net.thumbtack.onlineshop.database.dao;

import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.exeptions.ServerException;

import java.util.List;

public interface CategoryDao {
    void addCategory(Category category) throws ServerException;
    Category getCategory(int id);
    void updateCategory (int id, String name, Integer parentId);
    void deleteCategory (int id);
    List<Category> getParentsCategories();
    Category getCategoryById(int id);

    List<Category> getCategoriesById(List<Integer> categories);
}
