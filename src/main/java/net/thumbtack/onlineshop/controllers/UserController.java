package net.thumbtack.onlineshop.controllers;

import net.thumbtack.onlineshop.dto.request.user.*;
import net.thumbtack.onlineshop.dto.response.SuccessEmptyResponse;
import net.thumbtack.onlineshop.dto.response.user.*;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ValidationError;
import net.thumbtack.onlineshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static net.thumbtack.onlineshop.config.ServerConstants.COOKIE_NAME;

@Validated
@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path="/api/admins", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public UserInfoResponse registerAdministrator(@Valid @RequestBody AdministratorRegisterRequest dto, HttpServletResponse response) throws ServerException {
        userService.registerAdministrator(dto);
        return login(dto.getLogin(), dto.getPassword(), response);
    }

    @PostMapping(path="/api/clients", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public UserInfoResponse registerClient(@Valid @RequestBody ClientRegisterRequest dto, HttpServletResponse response) throws ServerException {
        userService.registerClient(dto);
        return login(dto.getLogin(), dto.getPassword(), response);
    }

    @PostMapping(path="/api/sessions", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public UserInfoResponse login(@Valid @RequestBody UserLoginRequest dto, HttpServletResponse response) throws ServerException {
        return login(dto.getLogin(), dto.getPassword(), response);
    }

    @DeleteMapping(path="/api/sessions", produces = MediaType.APPLICATION_JSON_VALUE )
    public SuccessEmptyResponse logout(@CookieValue(value = COOKIE_NAME) Cookie cookie, HttpServletResponse response) throws ServerException {
        userService.logout(cookie.getValue());
        return new SuccessEmptyResponse();
    }

    @GetMapping(path="/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE )
    public UserInfoResponse userInfo(@CookieValue(value = COOKIE_NAME) Cookie cookie, HttpServletResponse response) throws ServerException {
        return userService.getUserInfo(cookie.getValue());
    }

    @GetMapping(path="/api/clients", produces = MediaType.APPLICATION_JSON_VALUE )
    public List<ClientInfo> clientsInfo(@CookieValue(value = COOKIE_NAME) Cookie cookie, HttpServletResponse response) throws ServerException {
        return userService.getClientsInfo(cookie.getValue());
    }

    @PutMapping(path="/api/admins", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public AdministratorInfoResponse updateAdministratorProfile(@CookieValue(value = COOKIE_NAME) Cookie cookie,
            @Valid @RequestBody AdministratorEditRequest dto, HttpServletResponse response) throws ServerException {
        return userService.editAdministrator(dto, cookie.getValue());
    }

    @PutMapping(path="/api/clients", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ClientInfoResponse updateClientProfile(@CookieValue(value = COOKIE_NAME) Cookie cookie,
          @Valid @RequestBody ClientEditRequest dto, HttpServletResponse response) throws ServerException {
        return userService.editClient(dto, cookie.getValue());
    }

    private UserInfoResponse login(String login, String password, HttpServletResponse response) throws ServerException {
        UserLoginResponse r = userService.login(new UserLoginRequest(login, password));
        final Cookie cookie = new Cookie(COOKIE_NAME, r.getToken());
        response.addCookie(cookie);
        return r.getUserInfo();
    }
}
