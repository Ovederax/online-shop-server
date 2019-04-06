package net.thumbtack.onlineshop.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.response.AvailableSettingResponse;
import net.thumbtack.onlineshop.model.entity.Administrator;
import net.thumbtack.onlineshop.service.AdministratorService;
import net.thumbtack.onlineshop.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdministratorController {
    private AdministratorService service;

    @Autowired
    public AdministratorController(AdministratorService service) {
        this.service = service;
    }

    /*
    Этот endpoint Вам предстоит разработать самим. Необходимо предусмотреть как выдачу всей информации, так и выдачу
    информации по отдельным категориям или списку категорий, по товару или списку товаров, по клиентам и т.д. Желательно
    предусмотреть критерии упорядочения результирующей выборки. Ответ должен также содержать итоговые значения по выборке.
    Например, если возвращается список покупок некоторого клиента, в ответ надо включить их суммарную стоимость. Также
    необходимо предусмотреть вариант, когда выдаются только итоговые значения, без подробностей - в тех случаях, когда это имеет
    смысл.
    Ввиду того, что данный запрос может возвращать очень много данных, следует предусмотреть пагинацию результатов, введя
    параметры запроса “offset” (номер строки результата, с которой начать выдачу) и “limit” (количество строк). Итоговые значения при
    этом приводятся для возвращаемой выборки, а не для всего списка.
    */
//    @GetMapping(path="/api/purchases/параметры", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE )
//    public String getSummaryList(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody Dto dto, HttpServletResponse response) {
//        //по куки
//        return "{}";
//    }

    @GetMapping(path="/api/settings", produces = MediaType.APPLICATION_JSON_VALUE )
    public AvailableSettingResponse getSettings(@Valid @CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        return service.getSettings(cookie.getValue());
    }


    @GetMapping(path="/api/debug/clear" )
    public void clearDataBase(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        service.clearDataBase();
    }

}
