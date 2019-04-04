package net.thumbtack.onlineshop.dto.request.user;

import net.thumbtack.onlineshop.dto.response.ErrorContent;
import net.thumbtack.onlineshop.model.exeptions.enums.UserExceptionEnum;

import java.util.ArrayList;
import java.util.List;

public class AdministratorRegisterRequest {
    private String firstName;
    private String lastName;
    private String patronymic;
    private String position;
    private String login;
    private String password;

    public AdministratorRegisterRequest(String firstName, String lastName, String patronymic, String position, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.position = position;
        this.login = login;
        this.password = password;
    }

    public AdministratorRegisterRequest() {
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

    /**
     * Логин может содержать только латинские и русские буквы и цифры и
     * не может быть пустым.
     * Пароль может содержать любые символы и тоже
     * не может быть пустым.
     * Имя, фамилия и отчество администратора могут содержать только
     * русские буквы , пробелы и знак “минус” (используемый как тире).
     * */
    public List<ErrorContent> validate() {
        List<ErrorContent> list = new ArrayList<>();
        // нужно подумать как обобщить случаи, чтобы писать менше кода ...
        if(firstName == null || firstName.equals("")) {
            list.add(new ErrorContent(UserExceptionEnum.BAD_FIRST_NAME.toString(),
                    "firstName",
                    UserExceptionEnum.BAD_FIRST_NAME.getMessage()));
        }
        if(lastName == null || lastName.equals("")) {
            list.add(new ErrorContent(UserExceptionEnum.BAD_LAST_NAME.toString(),
                    "lastName",
                    UserExceptionEnum.BAD_LAST_NAME.getMessage()));
        }
        if(position == null || position.equals("")) {
            list.add(new ErrorContent(UserExceptionEnum.BAD_POSITION.toString(),
                    "position",
                    UserExceptionEnum.BAD_POSITION.getMessage()));
        }
        if(login == null || login.equals("")) {
            list.add(new ErrorContent(UserExceptionEnum.BAD_LOGIN.toString(),
                    "login",
                    UserExceptionEnum.BAD_LOGIN.getMessage()));
        }
        if(password == null || password.equals("")) {
            list.add(new ErrorContent(UserExceptionEnum.BAD_PASSWORD.toString(),
                    "password",
                    UserExceptionEnum.BAD_PASSWORD.getMessage()));
        }

        if (patronymic != null) {

        }
        return list;
    }
}
