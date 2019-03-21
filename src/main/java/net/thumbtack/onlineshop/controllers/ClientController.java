//package net.thumbtack.onlineshop.controllers;
//
//import demo.model.Dto;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.Valid;
//
//@RestController
//public class ClientController {
//    @PostMapping(path="/api/deposits", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String addMoneyDeposit(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody Dto dto, HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//
//    @GetMapping(path="/api/deposits", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String getMoneyDeposit(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody Dto dto, HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//
//
//    @PostMapping(path="/api/purchases", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String buyGoods(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody Dto dto, HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//}
