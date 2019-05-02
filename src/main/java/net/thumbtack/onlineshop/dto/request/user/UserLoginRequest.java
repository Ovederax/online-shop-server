package net.thumbtack.onlineshop.dto.request.user;

import net.thumbtack.onlineshop.model.exeptions.enums.ValidationError;
import net.thumbtack.onlineshop.validation.LoginFormat;
import net.thumbtack.onlineshop.validation.MaxNameLength;
import net.thumbtack.onlineshop.validation.MinPasswordLength;

import javax.validation.constraints.NotNull;

public class UserLoginRequest {
    @NotNull(message = ValidationError.LOGIN_CANNOT_BE_NULL)
    @MaxNameLength
    @LoginFormat
    private String login;

    @NotNull(message = ValidationError.PASSWORD_CANNOT_BE_NULL)
    @MinPasswordLength
    private String password;

    public UserLoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UserLoginRequest() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
