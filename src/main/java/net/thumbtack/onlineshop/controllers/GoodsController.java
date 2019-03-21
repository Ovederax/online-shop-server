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
//public class GoodsController {
//
//    @PostMapping(path="/api/products", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String addProduct(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
//                             @Valid @RequestBody Dto dto, HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//
//    @GetMapping(path="/api/products/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String getProduct(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
//                              @PathVariable int id, HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//
//    @PutMapping(path="/api/products/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String updateProduct(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
//                                 @PathVariable int id, @Valid @RequestBody Dto dto, HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//
//    @DeleteMapping(path="/api/products/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String deleteProduct(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
//                                @PathVariable int id, HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//
//    @GetMapping(path="/api/products", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String getProducts(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
//                                @RequestParam String cathegory, @RequestParam String order,
//                                HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//}
