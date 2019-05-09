package net.thumbtack.onlineshop.controllers;

import net.thumbtack.onlineshop.dto.response.AvailableSettingResponse;
import net.thumbtack.onlineshop.dto.response.summary.SummaryListResponse;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static net.thumbtack.onlineshop.config.ServerConstants.COOKIE_NAME;

@Validated
@RestController
public class AdministratorController {
    private AdministratorService service;

    @Autowired
    public AdministratorController(AdministratorService service) {
        this.service = service;
    }

    @GetMapping(path="/api/purchases", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public SummaryListResponse getSummaryList(@CookieValue(value = COOKIE_NAME) Cookie cookie,
                                              @RequestParam (value = "allInfo", required = false, defaultValue = "false") Boolean allInfo, // all or only summary
                                              @RequestParam (value = "categories", required = false) List<Integer> categories,
                                              @RequestParam (value = "products", required = false)   List<Integer> products,
                                              @RequestParam (value = "clients", required = false)    List<Integer> clients,
                                              @RequestParam (value = "offset", required = false, defaultValue = "1") Integer offset,
                                              @RequestParam(value = "limit", required = false, defaultValue = "100") Integer limit,
                                              HttpServletResponse response) throws ServerException {
        return service.getSummaryList( cookie.getValue(), allInfo, categories, products, clients, offset, limit );
    }

    @GetMapping(path="/api/settings", produces = MediaType.APPLICATION_JSON_VALUE )
    public AvailableSettingResponse getSettings(@CookieValue(value = COOKIE_NAME, required = false) Cookie cookie, HttpServletResponse response) {
        String cookieValue = cookie != null? cookie.getValue() : null;
        return service.getSettings(cookieValue);
    }


    @GetMapping(path="/api/debug/clear" )
    public void clearDataBase(HttpServletResponse response) throws ServerException {
        service.clearDataBase();
    }

}
