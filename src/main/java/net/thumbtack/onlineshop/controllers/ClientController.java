package net.thumbtack.onlineshop.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.thumbtack.onlineshop.dto.request.product.BuyProductRequest;
import net.thumbtack.onlineshop.dto.request.user.DepositMoneyRequest;
import net.thumbtack.onlineshop.dto.response.product.BuyProductResponse;
import net.thumbtack.onlineshop.dto.response.user.UserInfoResponse;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.service.ProductService;
import net.thumbtack.onlineshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static net.thumbtack.onlineshop.config.ServerConstants.COOKIE_NAME;

@RestController
public class ClientController {
    private UserService userService;
    private ProductService productService;

    @Autowired
    public ClientController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }
    @PostMapping(path="/api/deposits", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public UserInfoResponse addMoneyDeposit(@CookieValue(value = COOKIE_NAME) Cookie cookie, @Valid @RequestBody DepositMoneyRequest dto,
                                             HttpServletResponse response) throws ServerException {
        return userService.addMoneyDeposit(dto, cookie.getValue());
    }

    @GetMapping(path="/api/deposits", produces = MediaType.APPLICATION_JSON_VALUE )
    public UserInfoResponse getMoneyDeposit(@CookieValue(value = COOKIE_NAME) Cookie cookie, HttpServletResponse response) throws ServerException {
        return userService.getMoneyDeposit(cookie.getValue());
    }

    @PostMapping(path="/api/purchases", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public BuyProductResponse buyProduct(@CookieValue(value = COOKIE_NAME) Cookie cookie, @Valid @RequestBody BuyProductRequest dto,
                                         HttpServletResponse response) throws JsonProcessingException, ServerException {
        return productService.buyProduct(dto, cookie.getValue());
    }
}
