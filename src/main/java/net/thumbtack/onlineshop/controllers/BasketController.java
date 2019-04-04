package net.thumbtack.onlineshop.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.basket.BasketBuyProductRequest;
import net.thumbtack.onlineshop.dto.request.basket.BasketUpdateCountProductRequest;
import net.thumbtack.onlineshop.dto.request.basket.ProductAddInBasketsRequest;
import net.thumbtack.onlineshop.model.exeptions.UserException;
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
    private ObjectMapper mapper;
    private BasketService basketService;

    @Autowired
    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @PostMapping(path="/api/basket", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public String addProductIntoBasket(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody ProductAddInBasketsRequest dto, HttpServletResponse response) throws JsonProcessingException, UserException {
        response.setStatus(HttpServletResponse.SC_OK);
        return basketService.addProductInBasket(dto, cookie.getValue());
    }

    @DeleteMapping(path="/api/basket/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public String deleteProductFromBasket(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
                                          @PathVariable int id, HttpServletResponse response) throws UserException {
        response.setStatus(HttpServletResponse.SC_OK);
        basketService.deleteProductFromBasket(id, cookie.getValue());
        return "{}";
    }

    @PutMapping(path="/api/basket", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public String updateProductCountInBasket(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody BasketUpdateCountProductRequest dto, HttpServletResponse response) throws JsonProcessingException, UserException {
        response.setStatus(HttpServletResponse.SC_OK);
        return basketService.updateProductCount(dto, cookie.getValue());
    }

    @GetMapping(path="/api/basket", produces = MediaType.APPLICATION_JSON_VALUE )
    public String getProductsInBasket(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, HttpServletResponse response) throws JsonProcessingException, UserException {
        response.setStatus(HttpServletResponse.SC_OK);
        return basketService.getProductsInBasket(cookie.getValue());
    }

    @PostMapping(path="/api/purchases/basket", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public String buyProductsFromBasket(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody List<BasketBuyProductRequest> dto, HttpServletResponse response) throws UserException {
        response.setStatus(HttpServletResponse.SC_OK);
        return basketService.buyProduct(dto, cookie.getValue());
    }
}
