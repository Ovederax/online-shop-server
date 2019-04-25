package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.database.dao.CategoryDao;
import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.dto.request.cathegory.CategoryAddRequest;
import net.thumbtack.onlineshop.dto.request.cathegory.CategoryEditRequest;
import net.thumbtack.onlineshop.dto.response.cathegory.CategoryAddResponse;
import net.thumbtack.onlineshop.dto.response.cathegory.CategoryEditResponse;
import net.thumbtack.onlineshop.dto.response.cathegory.CategoryGetResponse;
import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService extends ServiceBase{
    private UserDao userDao;
    private CategoryDao categoryDao;

    @Autowired
    public CategoryService(UserDao userDao, CategoryDao categoryDao) {
        this.userDao = userDao;
        this.categoryDao = categoryDao;
    }

    public CategoryAddResponse addCategory(CategoryAddRequest dto, String token) throws ServerException {
        checkAdministratorPrivileges(userDao, token);
        Category parent = null;
        if(dto.getParentId() != null) {
            parent = categoryDao.findCategoryById(dto.getParentId());
        }
        Category category = new Category(dto.getName(), parent);
        categoryDao.addCategory(category);
        if(parent != null) {
            return new CategoryAddResponse(category.getId(), category.getName(), parent.getId(), parent.getName());
        }
        return new CategoryAddResponse(category.getId(), category.getName(), 0, null);
    }

    public CategoryGetResponse getCategory(int id, String token) throws ServerException {
        checkAdministratorPrivileges(userDao, token);
        Category category = categoryDao.getCategory(id);
        if(category == null) {
            throw new ServerException(ErrorCode.CATEGORY_NO_EXISTS);
        }
        Category parent = category.getParent();
        if(parent!=null) {
            return new CategoryGetResponse(category.getId(), category.getName(), parent.getId(), parent.getName());
        }
        return new CategoryGetResponse(category.getId(), category.getName(), 0, null);
    }

    public CategoryEditResponse updateCategory(int id, CategoryEditRequest dto, String token) throws ServerException {
        checkAdministratorPrivileges(userDao, token);
        if(dto.getParentId() != null) {
            Category c = categoryDao.getCategory(id);
            if (c.getParent() != null) {
                throw new ServerException(ErrorCode.BAD_SET_SUBCATEGORY_INTO_SUBCATEGORY);
            }
        }
        categoryDao.updateCategory(id, dto.getName(), dto.getParentId());
        Category category = categoryDao.getCategory(id);
        if(category == null) {
            throw new ServerException(ErrorCode.CATEGORY_NO_EXISTS);
        }
        Category parent = category.getParent();
        if(parent!=null) {
            return new CategoryEditResponse(category.getId(), category.getName(), parent.getId(), parent.getName());
        }
        return new CategoryEditResponse(category.getId(), category.getName(), 0, null);
    }

    public void deleteCategory(int id, String token) throws ServerException {
        checkAdministratorPrivileges(userDao, token);
        categoryDao.deleteCategory(id);
    }

    public List<CategoryGetResponse> getCategories(String token) throws ServerException {
        checkAdministratorPrivileges(userDao, token);
        List<Category> categories = categoryDao.getParentsCategories();
        List<CategoryGetResponse> out = new ArrayList<CategoryGetResponse>();

        /**Список выдается, отсортированный по именам категорий,
         за каждой из которых следуют ее подкатегории,
         также отсортированные по имени*/
        for(Category it : categories) {
            out.add(new CategoryGetResponse(it.getId(), it.getName(),0, null));
            for(Category subIt : it.getSubCategories()) {
                out.add((new CategoryGetResponse(subIt.getId(), subIt.getName(), it.getId(), it.getName())));
            }
        }
        return out;
    }
}
