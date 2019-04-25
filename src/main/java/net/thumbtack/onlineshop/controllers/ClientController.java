package net.thumbtack.onlineshop.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.product.ProductBuyRequest;
import net.thumbtack.onlineshop.dto.request.user.DepositMoneyRequest;
import net.thumbtack.onlineshop.dto.response.product.ProductBuyResponse;
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
import java.util.List;

@RestController
public class ClientController extends CommonController{
    private UserService userService;
    private ProductService productService;

    @Autowired
    public ClientController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }
    @PostMapping(path="/api/deposits", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public UserInfoResponse addMoneyDeposit(@CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody DepositMoneyRequest dto,
                                            BindingResult result, HttpServletResponse response) throws ServerException {
        verifyValidateServerException(result, cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        return userService.addMoneyDeposit(dto, cookie.getValue());
    }

    @GetMapping(path="/api/deposits", produces = MediaType.APPLICATION_JSON_VALUE )
    public UserInfoResponse getMoneyDeposit(@CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, HttpServletResponse response) throws ServerException {
        verifyCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        return userService.getMoneyDeposit(cookie.getValue());
    }

    @PostMapping(path="/api/purchases", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ProductBuyResponse buyProduct(@CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody ProductBuyRequest dto,
                                         BindingResult result, HttpServletResponse response) throws JsonProcessingException, ServerException {
        verifyValidateServerException(result, cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        return productService.buyProduct(dto, cookie.getValue());
    }
}
