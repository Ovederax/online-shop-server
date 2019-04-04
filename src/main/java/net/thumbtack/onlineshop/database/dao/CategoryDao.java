package net.thumbtack.onlineshop.database.dao;

import net.thumbtack.onlineshop.model.entity.Category;

import java.util.List;

public interface CategoryDao {
    int addCategory(String name, String parentId);
    Category getCategory(int id);
    void updateCategory (int id, String name, Integer parentId);
    void deleteCategory (int id);
    List<Category> getCategories();
}
