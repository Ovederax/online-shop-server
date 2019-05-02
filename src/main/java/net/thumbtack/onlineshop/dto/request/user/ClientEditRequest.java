package net.thumbtack.onlineshop.dto.request.user;

import net.thumbtack.onlineshop.model.exeptions.enums.ValidationError;
import net.thumbtack.onlineshop.validation.MaxNameLength;
import net.thumbtack.onlineshop.validation.MinPasswordLength;
import net.thumbtack.onlineshop.validation.NameFormat;
import net.thumbtack.onlineshop.validation.PhoneFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ClientEditRequest {
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

    @NotEmpty(message = ValidationError.ADDRESS_CANNOT_BE_EMPTY)
    private String address;

    @PhoneFormat
    private String phone;

    @NotNull(message = ValidationError.PASSWORD_CANNOT_BE_NULL)
    @MinPasswordLength
    private String oldPassword;

    @NotNull(message = ValidationError.PASSWORD_CANNOT_BE_NULL)
    @MinPasswordLength
    private String newPassword;

    public ClientEditRequest(String firstName, String lastName, String patronymic, String email, String address, String phone, String oldPassword, String newPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public ClientEditRequest() {
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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
