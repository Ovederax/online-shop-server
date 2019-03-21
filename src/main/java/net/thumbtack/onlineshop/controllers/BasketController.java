//package net.thumbtack.onlineshop.controllers;
//
//import demo.model.Dto;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.Valid;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class BasketController {
//    @PostMapping(path="/api/baskets", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String addGoodsIntoBasket(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody Dto dto, HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//
//    @DeleteMapping(path="/api/baskets/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String deleteGoodsFromBasket(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
//                                        @Valid @RequestBody Dto dto, @PathVariable int id, HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//
//    @PutMapping(path="/api/baskets", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String updateGoodsCountInBasket(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody Dto dto, HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//
//    @PostMapping(path="/api/purchases/baskets", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String byuGoodsFromBasket(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody Dto dto, HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//}
