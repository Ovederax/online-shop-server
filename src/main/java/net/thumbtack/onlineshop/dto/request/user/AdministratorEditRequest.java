package net.thumbtack.onlineshop.dto.request.user;

import net.thumbtack.onlineshop.model.exeptions.enums.ValidationError;
import net.thumbtack.onlineshop.validation.LoginFormat;
import net.thumbtack.onlineshop.validation.MaxNameLength;
import net.thumbtack.onlineshop.validation.MinPasswordLength;
import net.thumbtack.onlineshop.validation.NameFormat;

import javax.validation.constraints.NotNull;

public class AdministratorEditRequest {
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

    @NotNull(message = ValidationError.ADMINISTRATOR_POSITION_CANNOT_BE_NULL)
    private String position;

    @MinPasswordLength
    private String oldPassword;

    @MinPasswordLength
    private String newPassword;

    public AdministratorEditRequest(String firstName, String lastName, String patronymic, String position, String oldPassword, String newPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.position = position;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public AdministratorEditRequest() {
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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
