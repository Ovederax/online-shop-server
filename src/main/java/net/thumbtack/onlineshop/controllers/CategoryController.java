package net.thumbtack.onlineshop.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.cathegory.CategoryAddRequest;
import net.thumbtack.onlineshop.dto.request.cathegory.CategoryEditRequest;
import net.thumbtack.onlineshop.dto.response.SuccessEmptyResponse;
import net.thumbtack.onlineshop.dto.response.cathegory.CategoryAddResponse;
import net.thumbtack.onlineshop.dto.response.cathegory.CategoryEditResponse;
import net.thumbtack.onlineshop.dto.response.cathegory.CategoryGetResponse;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    public CategoryAddResponse addCategory(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody CategoryAddRequest dto, HttpServletResponse response) throws JsonProcessingException, ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        return categoryService.addCategory(dto, cookie.getValue());
    }
    @GetMapping(path="/api/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public CategoryGetResponse getCategory(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @PathVariable int id, HttpServletResponse response) throws JsonProcessingException, ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        return categoryService.getCategory(id, cookie.getValue());
    }
    @PutMapping(path="/api/categories/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public CategoryEditResponse updateCategory(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
                                               @PathVariable int id, @Valid @RequestBody CategoryEditRequest dto, HttpServletResponse response) throws JsonProcessingException, ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        return categoryService.updateCategory(id, dto, cookie.getValue());
    }
    @DeleteMapping(path="/api/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public SuccessEmptyResponse deleteCategory(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
                                               @PathVariable int id, HttpServletResponse response) throws ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        categoryService.deleteCategory(id, cookie.getValue());
        return new SuccessEmptyResponse();
    }

    @GetMapping(path="/api/categories", produces = MediaType.APPLICATION_JSON_VALUE )
    public List<CategoryGetResponse> getCategories(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, HttpServletResponse response) throws JsonProcessingException, ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        return categoryService.getCategories(cookie.getValue());
    }
}
