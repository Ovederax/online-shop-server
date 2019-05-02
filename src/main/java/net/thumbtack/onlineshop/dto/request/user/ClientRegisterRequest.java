package net.thumbtack.onlineshop.dto.request.user;

import net.thumbtack.onlineshop.model.exeptions.enums.ValidationError;
import net.thumbtack.onlineshop.validation.*;

import javax.validation.constraints.*;

public class ClientRegisterRequest {
    @NotNull(message = ValidationError.FIRST_NAME_CANNOT_BE_NULL)
    @MaxNameLength
    @NameFormat
    private String firstName;

    @NotNull(message = ValidationError.LAST_NAME_CANNOT_BE_NULL)
    @MaxNameLength
    @NameFormat
    private String lastName;

    @MaxNameLength
    @NameFormat
    private String patronymic;

    @Email
    private String email;

    @NotBlank(message = ValidationError.ADDRESS_CANNOT_BE_EMPTY)
    private String address;

    @PhoneFormat
    private String phone;

    @NotNull(message = ValidationError.LOGIN_CANNOT_BE_NULL)
    @MaxNameLength
    @LoginFormat
    private String login;

    @NotNull(message = ValidationError.PASSWORD_CANNOT_BE_NULL)
    @MinPasswordLength
    private String password;

    public ClientRegisterRequest(String firstName, String lastName, String patronymic, String email, String address, String phone, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.login = login;
        this.password = password;
    }

    public ClientRegisterRequest() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
