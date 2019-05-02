package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.database.dao.CategoryDao;
import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.dto.request.category.AddCategoryRequest;
import net.thumbtack.onlineshop.dto.request.category.EditCategoryRequest;
import net.thumbtack.onlineshop.dto.response.category.AddCategoryResponse;
import net.thumbtack.onlineshop.dto.response.category.EditCategoryResponse;
import net.thumbtack.onlineshop.dto.response.category.GetCategoryResponse;
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

    public AddCategoryResponse addCategory(AddCategoryRequest dto, String token) throws ServerException {
        checkAdministratorPrivileges(userDao, token);
        Category parent = null;
        if(dto.getParentId() != null) {
            parent = categoryDao.getCategoryById(dto.getParentId());
        }
        Category category = new Category(dto.getName(), parent);
        categoryDao.addCategory(category);
        if(parent != null) {
            return new AddCategoryResponse(category.getId(), category.getName(), parent.getId(), parent.getName());
        }
        return new AddCategoryResponse(category.getId(), category.getName(), 0, null);
    }

    public GetCategoryResponse getCategory(int id, String token) throws ServerException {
        checkAdministratorPrivileges(userDao, token);
        Category category = categoryDao.getCategory(id);
        if(category == null) {
            throw new ServerException(ErrorCode.CATEGORY_NO_EXISTS);
        }
        Category parent = category.getParent();
        if(parent!=null) {
            return new GetCategoryResponse(category.getId(), category.getName(), parent.getId(), parent.getName());
        }
        return new GetCategoryResponse(category.getId(), category.getName(), 0, null);
    }

    public EditCategoryResponse updateCategory(int id, EditCategoryRequest dto, String token) throws ServerException {
        checkAdministratorPrivileges(userDao, token);
        if(dto.getParentId() != null) {
            Category category = categoryDao.getCategory(id);
            if (category.getParent() != null) {
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
            return new EditCategoryResponse(category.getId(), category.getName(), parent.getId(), parent.getName());
        }
        return new EditCategoryResponse(category.getId(), category.getName(), 0, null);
    }

    public void deleteCategory(int id, String token) throws ServerException {
        checkAdministratorPrivileges(userDao, token);
        categoryDao.deleteCategory(id);
    }

    public List<GetCategoryResponse> getCategories(String token) throws ServerException {
        checkAdministratorPrivileges(userDao, token);
        List<Category> categories = categoryDao.getParentsCategories();
        List<GetCategoryResponse> out = new ArrayList<GetCategoryResponse>();

        /**Список выдается, отсортированный по именам категорий,
         за каждой из которых следуют ее подкатегории,
         также отсортированные по имени*/
        for(Category it : categories) {
            out.add(new GetCategoryResponse(it.getId(), it.getName(),0, null));
            for(Category subIt : it.getSubCategories()) {
                out.add((new GetCategoryResponse(subIt.getId(), subIt.getName(), it.getId(), it.getName())));
            }
        }
        return out;
    }
}
