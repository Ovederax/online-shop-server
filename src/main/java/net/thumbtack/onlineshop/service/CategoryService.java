package net.thumbtack.onlineshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.database.dao.CategoryDao;
import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.dto.request.cathegory.CategoryAddRequest;
import net.thumbtack.onlineshop.dto.request.cathegory.CategoryEditRequest;
import net.thumbtack.onlineshop.dto.response.cathegory.CategoryAddResponse;
import net.thumbtack.onlineshop.dto.response.cathegory.CategoryEditResponse;
import net.thumbtack.onlineshop.dto.response.cathegory.CategoryGetResponse;
import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.exeptions.CategoryException;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.UserException;
import net.thumbtack.onlineshop.model.exeptions.enums.CategoryExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CategoryService {
    private @Autowired ObjectMapper mapper;
    private CategoryDao categoryDao;
    private UserService userService;

    @Autowired
    public CategoryService(CategoryDao categoryDao, UserService userService) {
        this.categoryDao = categoryDao;
        this.userService = userService;
    }

    public String addCategory(CategoryAddRequest dto, String token) throws UserException, JsonProcessingException, CategoryException {
        userService.checkAdministratorPrivileges(token);
        int id = categoryDao.addCategory(dto.getName(), dto.getParentId());
        Category c = categoryDao.getCategory(id);
        Category p = c.getParent();
        if(p!=null) {
            return mapper.writeValueAsString(new CategoryAddResponse(c.getId(), c.getName(), p.getId(), p.getName()));
        }
        return mapper.writeValueAsString(new CategoryAddResponse(c.getId(), c.getName(), 0, null));
    }

    public String getCategory(int id, String token) throws UserException, JsonProcessingException, CategoryException {
        userService.checkAdministratorPrivileges(token);
        Category c = categoryDao.getCategory(id);
        if(c == null) {
            throw new CategoryException(CategoryExceptionEnum.CATEGORY_NO_EXISTS);
        }
        Category p = c.getParent();
        if(p!=null) {
            return mapper.writeValueAsString(new CategoryGetResponse(c.getId(), c.getName(), p.getId(), p.getName()));
        }
        return mapper.writeValueAsString(new CategoryGetResponse(c.getId(), c.getName(), 0, null));
    }

    public String updateCategory(int id, CategoryEditRequest dto, String token) throws JsonProcessingException, ServerException {
        userService.checkAdministratorPrivileges(token);
        if(dto.getParentId() != null) {
            Category c = categoryDao.getCategory(id);
            if (c.getParent() != null) {
                throw new CategoryException(CategoryExceptionEnum.BAD_SET_SUBCATEGORY_INTO_SUBCATEGORY);
            }
        }
        categoryDao.updateCategory(id, dto.getName(), dto.getParentId());
        Category c = categoryDao.getCategory(id);
        if(c == null) {
            throw new CategoryException(CategoryExceptionEnum.CATEGORY_NO_EXISTS);
        }
        Category p = c.getParent();
        if(p!=null) {
            return mapper.writeValueAsString(new CategoryEditResponse(c.getId(), c.getName(), p.getId(), p.getName()));
        }
        return mapper.writeValueAsString(new CategoryEditResponse(c.getId(), c.getName(), 0, null));
    }

    public void deleteCategory(int id, String token) throws UserException {
        userService.checkAdministratorPrivileges(token);
        categoryDao.deleteCategory(id);
    }

    public String getCategories(String token) throws UserException, JsonProcessingException {
        userService.checkAdministratorPrivileges(token);
        List<Category> categories = categoryDao.getCategories();
        List<CategoryGetResponse> out = new ArrayList<CategoryGetResponse>();


        for(Category it : categories) {
            if(it.getParent() != null) {
                out.add(new CategoryGetResponse(it.getId(), it.getName(), it.getParent().getId(), it.getParent().getName()));
            } else {
                out.add(new CategoryGetResponse(it.getId(), it.getName(), 0, null));
            }
        }
        // ??? нужно подумать ???
        out.sort(Comparator.comparing(CategoryGetResponse::getName));
        return mapper.writeValueAsString(out);


        /**Список выдается, отсортированный по именам категорий,
         за каждой из которых следуют ее подкатегории,
         также отсортированные по имени*/

    }
}
