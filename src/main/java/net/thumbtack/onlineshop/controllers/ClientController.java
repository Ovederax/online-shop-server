package net.thumbtack.onlineshop.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.product.ProductBuyRequest;
import net.thumbtack.onlineshop.dto.request.user.DepositMoneyRequest;
import net.thumbtack.onlineshop.dto.response.user.UserInfoResponse;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.service.ProductService;
import net.thumbtack.onlineshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
public class ClientController {
    @Autowired
    private ObjectMapper mapper;
    private UserService userService;
    private ProductService productService;

    @Autowired
    public ClientController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }
    @PostMapping(path="/api/deposits", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public UserInfoResponse addMoneyDeposit(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody DepositMoneyRequest dto, HttpServletResponse response) throws ServerException, JsonProcessingException {
        response.setStatus(HttpServletResponse.SC_OK);
        userService.addMoneyDeposit(dto, cookie.getValue());
        return userService.getUserInfo(cookie.getValue());
    }

    @GetMapping(path="/api/deposits", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public UserInfoResponse getMoneyDeposit(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, HttpServletResponse response) throws ServerException, JsonProcessingException {
        response.setStatus(HttpServletResponse.SC_OK);
        return userService.getMoneyDeposit(cookie.getValue());
    }


    @PostMapping(path="/api/purchases", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public List<ProductBuyRequest> buyProduct(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody ProductBuyRequest dto, HttpServletResponse response) throws JsonProcessingException, ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        return productService.buyProduct(dto, cookie.getValue());
    }
}
