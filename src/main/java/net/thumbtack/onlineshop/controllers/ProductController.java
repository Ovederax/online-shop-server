package net.thumbtack.onlineshop.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.product.ProductAddRequest;
import net.thumbtack.onlineshop.dto.request.product.ProductEditRequest;
import net.thumbtack.onlineshop.model.exeptions.UserException;
import net.thumbtack.onlineshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ProductController {
    @Autowired private ObjectMapper mapper;
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(path="/api/products", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public String addProduct(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
                             @Valid @RequestBody ProductAddRequest dto, HttpServletResponse response) throws UserException, JsonProcessingException {
        response.setStatus(HttpServletResponse.SC_OK);
        return productService.addProduct(dto, cookie.getValue());
    }

    @GetMapping(path="/api/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public String getProduct(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
                              @PathVariable int id, HttpServletResponse response) throws JsonProcessingException, UserException {
        response.setStatus(HttpServletResponse.SC_OK);
        return productService.getProduct(id, cookie.getValue());
    }

    @PutMapping(path="/api/products/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public String updateProduct(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
                                @PathVariable int id, @Valid @RequestBody ProductEditRequest dto, HttpServletResponse response) throws UserException, JsonProcessingException {
        response.setStatus(HttpServletResponse.SC_OK);
        return productService.updateProduct(id, dto, cookie.getValue());
    }

    @DeleteMapping(path="/api/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public String deleteProduct(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
                                @PathVariable int id, HttpServletResponse response) throws UserException {
        response.setStatus(HttpServletResponse.SC_OK);
        productService.deleteProduct(id, cookie.getValue());
        return "{}";
    }

    @GetMapping(path="/api/products", produces = MediaType.APPLICATION_JSON_VALUE )
    public String getProducts(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
                            @RequestParam String category, @RequestParam String order, HttpServletResponse response) throws UserException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        return productService.getProductsList(category, order, cookie.getValue());
    }
}
