package net.thumbtack.onlineshop.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.thumbtack.onlineshop.dto.request.basket.BuyProductFromBasketRequest;
import net.thumbtack.onlineshop.dto.request.basket.BasketUpdateCountProductRequest;
import net.thumbtack.onlineshop.dto.request.basket.AddProductToBasketsRequest;
import net.thumbtack.onlineshop.dto.response.SuccessEmptyResponse;
import net.thumbtack.onlineshop.dto.response.basket.BuyProductFromBasketResponse;
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
import java.util.List;

import static net.thumbtack.onlineshop.config.ServerConstants.COOKIE_NAME;

@RestController
public class BasketController {
    private BasketService basketService;

    @Autowired
    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @PostMapping(path="/api/basket", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public List<ProductInBasketResponse> addProductIntoBasket(@CookieValue(value = COOKIE_NAME) Cookie cookie,
                                                              @Valid @RequestBody AddProductToBasketsRequest dto, HttpServletResponse response) throws JsonProcessingException, ServerException {
        return basketService.addProductToBasket(dto, cookie.getValue());
    }

    @DeleteMapping(path="/api/basket/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public SuccessEmptyResponse deleteProductFromBasket(@CookieValue(value = COOKIE_NAME) Cookie cookie,
                                                        @PathVariable int id, HttpServletResponse response) throws ServerException {
        basketService.deleteProductFromBasket(id, cookie.getValue());
        return new SuccessEmptyResponse();
    }

    @PutMapping(path="/api/basket", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public List<ProductInBasketResponse> updateProductCountInBasket(@CookieValue(value = COOKIE_NAME) Cookie cookie,
                                        @Valid @RequestBody BasketUpdateCountProductRequest dto, HttpServletResponse response) throws ServerException {
        return basketService.updateProductCount(dto, cookie.getValue());
    }

    @GetMapping(path="/api/basket", produces = MediaType.APPLICATION_JSON_VALUE )
    public List<ProductInBasketResponse> getProductsInBasket(@CookieValue(value = COOKIE_NAME) Cookie cookie,
                                                            HttpServletResponse response) throws ServerException {
        return basketService.getProductsInBasket(cookie.getValue());
    }

    @PostMapping(path="/api/purchases/basket", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public BuyProductFromBasketResponse buyProductsFromBasket(@CookieValue(value = COOKIE_NAME) Cookie cookie,
                                                              @Valid @RequestBody List<BuyProductFromBasketRequest> dto, HttpServletResponse response) throws ServerException {
        return basketService.buyProductFromBasket(dto, cookie.getValue());
    }
}
