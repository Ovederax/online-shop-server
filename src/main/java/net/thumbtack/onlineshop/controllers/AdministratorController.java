//package net.thumbtack.onlineshop.controllers;
//
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
//public class AdministratorController {
//    /*
//    Этот endpoint Вам предстоит разработать самим. Необходимо предусмотреть как выдачу всей информации, так и выдачу
//    информации по отдельным категориям или списку категорий, по товару или списку товаров, по клиентам и т.д. Желательно
//    предусмотреть критерии упорядочения результирующей выборки. Ответ должен также содержать итоговые значения по выборке.
//    Например, если возвращается список покупок некоторого клиента, в ответ надо включить их суммарную стоимость. Также
//    необходимо предусмотреть вариант, когда выдаются только итоговые значения, без подробностей - в тех случаях, когда это имеет
//    смысл.
//    Ввиду того, что данный запрос может возвращать очень много данных, следует предусмотреть пагинацию результатов, введя
//    параметры запроса “offset” (номер строки результата, с которой начать выдачу) и “limit” (количество строк). Итоговые значения при
//    этом приводятся для возвращаемой выборки, а не для всего списка.
//    */
//    @GetMapping(path="/api/purchases/параметры", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String getСводнаяВедомость(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody Dto dto, HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//
//    /**
//     Параметр “cookie” для этого запроса не является обязательным. Если он передается, то для администратора выдаются доступные
//     ему настройки, а для клиента - доступные ему. Если cookie не передается в запросе, возвращается список настроек, доступных до
//     выполнения операции “Login”
//     */
//
//    @GetMapping(path="/api/settings", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String getSettings(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody Dto dto, HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//
//    //Удаляет все записи в БД. Метод предназначен для отладки, в production должен быть отключен.
//    @GetMapping(path="/api/debug/clear", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String clearDataBase(HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }
//
//}
