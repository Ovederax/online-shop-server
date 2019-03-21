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
    private UserService userService;
    private Gson gson = new Gson();
    @Autowired private ObjectMapper mapper;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path="/api/admins", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public String registerAdministrator(@Valid @RequestBody AdministratorRegisterRequest dto, HttpServletResponse response) {
        try {
            userService.registerAdministrator(dto);
            String token = userService.login(new UserLoginRequest(dto.getLogin(), dto.getPassword()));
            final Cookie cookie = new Cookie("JAVASESSIONID", token);
            response.addCookie(cookie);
            response.setStatus(HttpServletResponse.SC_OK);
            return userService.getUserInfo(token);
        } catch (ServerErrorException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping(path="/api/clients", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public String registerClient(@Valid @RequestBody ClientRegisterRequest dto, HttpServletResponse response) {
        try {
            userService.registerClient(dto);
            String token = userService.login(new UserLoginRequest(dto.getLogin(), dto.getPassword()));
            final Cookie cookie = new Cookie("JAVASESSIONID", token);
            response.addCookie(cookie);
            response.setStatus(HttpServletResponse.SC_OK);
            return userService.getUserInfo(token);
        } catch (ServerErrorException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping(path="/api/sessions", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public String login(@Valid @RequestBody UserLoginRequest dto, HttpServletResponse response) {
        try {
            String token = userService.login(new UserLoginRequest(dto.getLogin(), dto.getPassword()));
            final Cookie cookie = new Cookie("JAVASESSIONID", token);
            response.addCookie(cookie);
            response.setStatus(HttpServletResponse.SC_OK);
            return userService.getUserInfo(token);
        } catch (ServerErrorException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @DeleteMapping(path="/api/sessions", produces = MediaType.APPLICATION_JSON_VALUE )
    public String logout(@CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, HttpServletResponse response) {
        try {
            userService.logout(cookie.getValue());
        } catch (ServerErrorException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return "{}";
    }

    @GetMapping(path="/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE )
    public String userInfo(@CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            return userService.getUserInfo(cookie.getValue());
        } catch (ServerErrorException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(path="/api/clients", produces = MediaType.APPLICATION_JSON_VALUE )
    public String clientsInfo(@CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            return mapper.writeValueAsString(userService.getClientsInfo(cookie.getValue()));
        } catch (ServerErrorException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (ServerException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PutMapping(path="/api/admins", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public String updateAdministratorProfile(@CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody AdministratorEditRequest dto, HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            userService.editAdministrator(dto, cookie.getValue());
            return userService.getUserInfo(cookie.getValue());
        } catch (ServerErrorException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PutMapping(path="/api/clients", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public String updateClientProfile(@CookieValue(value = "JAVASESSIONID", required = false) Cookie cookie, @Valid @RequestBody ClientEditRequest dto, HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            userService.editClient(dto, cookie.getValue());
            return userService.getUserInfo(cookie.getValue());
        } catch (ServerErrorException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
