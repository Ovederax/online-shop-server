package net.thumbtack.onlineshop.database.dao;

import net.thumbtack.onlineshop.model.entity.Category;

import java.util.List;

public interface CategoryDao {
    void addCategory(Category category);
    Category getCategory(int id);
    void updateCategory (int id, String name, Integer parentId);
    void deleteCategory (int id);
    List<Category> getParentsCategories();
    Category findCategoryById(int id);

    List<Category> findCategoriesById(List<Integer> categories);
}
