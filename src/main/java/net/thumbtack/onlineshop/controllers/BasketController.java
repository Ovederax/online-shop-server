package net.thumbtack.onlineshop.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.basket.BasketBuyProductRequest;
import net.thumbtack.onlineshop.dto.request.basket.BasketUpdateCountProductRequest;
import net.thumbtack.onlineshop.dto.request.basket.ProductAddInBasketsRequest;
import net.thumbtack.onlineshop.dto.response.SuccessEmptyResponse;
import net.thumbtack.onlineshop.dto.response.basket.BasketBuyProductResponse;
import net.thumbtack.onlineshop.dto.response.basket.BasketResponse;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
public class BasketController {
    @Autowired
    private BasketService basketService;

    @Autowired
    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @PostMapping(path="/api/basket", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public List<BasketResponse> addProductIntoBasket(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody ProductAddInBasketsRequest dto, HttpServletResponse response) throws JsonProcessingException, ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        return basketService.addProductInBasket(dto, cookie.getValue());
    }

    @DeleteMapping(path="/api/basket/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public SuccessEmptyResponse deleteProductFromBasket(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
                                                        @PathVariable int id, HttpServletResponse response) throws ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        basketService.deleteProductFromBasket(id, cookie.getValue());
        return new SuccessEmptyResponse();
    }

    @PutMapping(path="/api/basket", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public List<BasketResponse> updateProductCountInBasket(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody BasketUpdateCountProductRequest dto, HttpServletResponse response) throws JsonProcessingException, ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        return basketService.updateProductCount(dto, cookie.getValue());
    }

    @GetMapping(path="/api/basket", produces = MediaType.APPLICATION_JSON_VALUE )
    public List<BasketResponse> getProductsInBasket(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, HttpServletResponse response) throws JsonProcessingException, ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        return basketService.getProductsInBasket(cookie.getValue());
    }

    @PostMapping(path="/api/purchases/basket", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public BasketBuyProductResponse buyProductsFromBasket(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody List<BasketBuyProductRequest> dto, HttpServletResponse response) throws ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        return basketService.buyProduct(dto, cookie.getValue());
    }
}
