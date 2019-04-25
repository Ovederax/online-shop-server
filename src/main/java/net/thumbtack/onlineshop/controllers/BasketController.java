package net.thumbtack.onlineshop.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.basket.BasketBuyProductRequest;
import net.thumbtack.onlineshop.dto.request.basket.BasketUpdateCountProductRequest;
import net.thumbtack.onlineshop.dto.request.basket.AddProductToBasketsRequest;
import net.thumbtack.onlineshop.dto.response.SuccessEmptyResponse;
import net.thumbtack.onlineshop.dto.response.basket.BasketBuyProductResponse;
import net.thumbtack.onlineshop.dto.response.basket.ProductInBasketResponse;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BasketController extends CommonController{
    private BasketService basketService;

    @Autowired
    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @PostMapping(path="/api/basket", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public List<ProductInBasketResponse> addProductIntoBasket(@CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
                                                              @Valid @RequestBody AddProductToBasketsRequest dto, BindingResult result, HttpServletResponse response) throws JsonProcessingException, ServerException {
        verifyValidateServerException(result, cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        return basketService.addProductToBasket(dto, cookie.getValue());
    }

    @DeleteMapping(path="/api/basket/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public SuccessEmptyResponse deleteProductFromBasket(@CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
                                                        @PathVariable int id, HttpServletResponse response) throws ServerException {
        verifyCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        basketService.deleteProductFromBasket(id, cookie.getValue());
        return new SuccessEmptyResponse();
    }

    @PutMapping(path="/api/basket", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public List<ProductInBasketResponse> updateProductCountInBasket(@CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
                                        @Valid @RequestBody BasketUpdateCountProductRequest dto, BindingResult result, HttpServletResponse response) throws ServerException {
        verifyValidateServerException(result, cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        return basketService.updateProductCount(dto, cookie.getValue());
    }

    @GetMapping(path="/api/basket", produces = MediaType.APPLICATION_JSON_VALUE )
    public List<ProductInBasketResponse> getProductsInBasket(@CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
                                                            HttpServletResponse response) throws ServerException {
        verifyCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        return basketService.getProductsInBasket(cookie.getValue());
    }

    @PostMapping(path="/api/purchases/basket", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public BasketBuyProductResponse buyProductsFromBasket(@CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
                                                          @Valid @RequestBody List<BasketBuyProductRequest> dto, BindingResult result, HttpServletResponse response) throws ServerException {
        verifyValidateServerException(result, cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        return basketService.buyProductFromBasket(dto, cookie.getValue());
    }
}
