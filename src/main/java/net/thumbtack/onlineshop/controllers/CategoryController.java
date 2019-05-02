package net.thumbtack.onlineshop.controllers;

import net.thumbtack.onlineshop.dto.request.category.AddCategoryRequest;
import net.thumbtack.onlineshop.dto.request.category.EditCategoryRequest;
import net.thumbtack.onlineshop.dto.response.SuccessEmptyResponse;
import net.thumbtack.onlineshop.dto.response.category.AddCategoryResponse;
import net.thumbtack.onlineshop.dto.response.category.EditCategoryResponse;
import net.thumbtack.onlineshop.dto.response.category.GetCategoryResponse;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @PostMapping(path="/api/categories", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public AddCategoryResponse addCategory(@CookieValue(value = "JAVASESSIONID") Cookie cookie,
                                           @Valid @RequestBody AddCategoryRequest dto, HttpServletResponse response) throws ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        return categoryService.addCategory(dto, cookie.getValue());
    }
    @GetMapping(path="/api/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public GetCategoryResponse getCategory(@CookieValue(value = "JAVASESSIONID") Cookie cookie,
                                           @PathVariable int id, HttpServletResponse response) throws ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        return categoryService.getCategory(id, cookie.getValue());
    }
    @PutMapping(path="/api/categories/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public EditCategoryResponse updateCategory(@CookieValue(value = "JAVASESSIONID") Cookie cookie,
                                               @PathVariable int id, @Valid @RequestBody EditCategoryRequest dto, HttpServletResponse response) throws ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        return categoryService.updateCategory(id, dto, cookie.getValue());
    }
    @DeleteMapping(path="/api/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public SuccessEmptyResponse deleteCategory(@CookieValue(value = "JAVASESSIONID") Cookie cookie,
                                               @PathVariable int id, HttpServletResponse response) throws ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        categoryService.deleteCategory(id, cookie.getValue());
        return new SuccessEmptyResponse();
    }

    @GetMapping(path="/api/categories", produces = MediaType.APPLICATION_JSON_VALUE )
    public List<GetCategoryResponse> getCategories(@CookieValue(value = "JAVASESSIONID") Cookie cookie,
                                                   HttpServletResponse response) throws ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        return categoryService.getCategories(cookie.getValue());
    }
}
