package net.thumbtack.onlineshop.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import net.thumbtack.onlineshop.dto.request.user.*;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.validation.Valid;

@RestController
public class UserController {
    @Autowired private ObjectMapper mapper;
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path="/api/admins", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public String registerAdministrator(@Valid @RequestBody AdministratorRegisterRequest dto, HttpServletResponse response) throws ServerException {
        userService.registerAdministrator(dto);
        String token = userService.login(new UserLoginRequest(dto.getLogin(), dto.getPassword()));
        final Cookie cookie = new Cookie("JAVASESSIONID", token);
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        return userService.getUserInfo(token);
    }

    @PostMapping(path="/api/clients", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public String registerClient(@Valid @RequestBody ClientRegisterRequest dto, HttpServletResponse response) throws ServerException {
        userService.registerClient(dto);
        String token = userService.login(new UserLoginRequest(dto.getLogin(), dto.getPassword()));
        final Cookie cookie = new Cookie("JAVASESSIONID", token);
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        return userService.getUserInfo(token);
    }

    @PostMapping(path="/api/sessions", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public String login(@Valid @RequestBody UserLoginRequest dto, HttpServletResponse response) throws ServerException {
        String token = userService.login(new UserLoginRequest(dto.getLogin(), dto.getPassword()));
        final Cookie cookie = new Cookie("JAVASESSIONID", token);
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        return userService.getUserInfo(token);
    }

    @DeleteMapping(path="/api/sessions", produces = MediaType.APPLICATION_JSON_VALUE )
    public String logout(@CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, HttpServletResponse response) throws ServerException {
        userService.logout(cookie.getValue());
        return "{}";
    }

    @GetMapping(path="/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE )
    public String userInfo(@CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, HttpServletResponse response) throws ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
        return userService.getUserInfo(cookie.getValue());
    }

    @GetMapping(path="/api/clients", produces = MediaType.APPLICATION_JSON_VALUE )
    public String clientsInfo(@CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, HttpServletResponse response) throws ServerException, JsonProcessingException {
        response.setStatus(HttpServletResponse.SC_OK);
        return mapper.writeValueAsString(userService.getClientsInfo(cookie.getValue()));
    }

    @PutMapping(path="/api/admins", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public String updateAdministratorProfile(@CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody AdministratorEditRequest dto, HttpServletResponse response) throws ServerException {
        response.setStatus(HttpServletResponse.SC_OK);
    userService.editAdministrator(dto, cookie.getValue());
        return userService.getUserInfo(cookie.getValue());
    }

    @PutMapping(path="/api/clients", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public String updateClientProfile(@CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody ClientEditRequest dto, HttpServletResponse response) throws ServerException {
    response.setStatus(HttpServletResponse.SC_OK);
    userService.editClient(dto, cookie.getValue());
    return userService.getUserInfo(cookie.getValue());
    }
}
