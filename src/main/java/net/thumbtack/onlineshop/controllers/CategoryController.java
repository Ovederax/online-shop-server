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
//public class CategoryController {
//    @PostMapping(path="/api/categories", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String addCategory(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody Dto dto, HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//    @GetMapping(path="/api/categories/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String getCategory(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,@PathVariable int id, HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//    @PutMapping(path="/api/categories/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String updateCategory(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
//                                 @PathVariable int id, @Valid @RequestBody Dto dto, HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//    @DeleteMapping(path="/api/categories/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String deleteCategory(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie,
//                                 @PathVariable int id, HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//
//    @GetMapping(path="/api/categories", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String getCategories(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//}
