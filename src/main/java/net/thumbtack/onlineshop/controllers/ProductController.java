package net.thumbtack.onlineshop.controllers;

import net.thumbtack.onlineshop.dto.request.product.AddProductRequest;
import net.thumbtack.onlineshop.dto.request.product.EditProductRequest;
import net.thumbtack.onlineshop.dto.response.SuccessEmptyResponse;
import net.thumbtack.onlineshop.dto.response.product.GetProductResponse;
import net.thumbtack.onlineshop.dto.response.product.ProductResponse;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(path="/api/products", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    // REVU create static final String COOKIE_NAME
    public ProductResponse addProduct(@CookieValue(value = "JAVASESSIONID") Cookie cookie,
                                      @Valid @RequestBody AddProductRequest dto, HttpServletResponse response) throws ServerException {
        // REVU setStatus is unnecessary and HttpServletResponse as parameter is unnecessary too
    	response.setStatus(HttpServletResponse.SC_OK);
        return productService.addProduct(dto, cookie.getValue());
    }

    @GetMapping(path="/api/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public GetProductResponse getProduct(@CookieValue(value = "JAVASESSIONID") Cookie cookie,
                                         @PathVariable int id, HttpServletResponse response) throws ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        return productService.getProduct(id, cookie.getValue());
    }

    @PutMapping(path="/api/products/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ProductResponse updateProduct(@CookieValue(value = "JAVASESSIONID") Cookie cookie,
                                         @PathVariable int id, @Valid @RequestBody EditProductRequest dto, HttpServletResponse response) throws ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        return productService.updateProduct(id, dto, cookie.getValue());
    }

    @DeleteMapping(path="/api/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public SuccessEmptyResponse deleteProduct(@CookieValue(value = "JAVASESSIONID") Cookie cookie,
                                              @PathVariable int id, HttpServletResponse response) throws ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        productService.deleteProduct(id, cookie.getValue());
        return new SuccessEmptyResponse();
    }

    @GetMapping(path="/api/products", produces = MediaType.APPLICATION_JSON_VALUE )
    public List<GetProductResponse> getProducts(@CookieValue(value = "JAVASESSIONID") Cookie cookie,
                                                @RequestParam(required = false) List<Integer> categoriesId, @RequestParam(required = false) String order, HttpServletResponse response) throws ServerException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        return productService.getProductsList(categoriesId, order, cookie.getValue());
    }
}
