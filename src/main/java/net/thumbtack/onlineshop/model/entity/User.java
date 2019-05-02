package net.thumbtack.onlineshop.model.entity;

import java.util.Objects;

public class User {
    private int id;
    private String firstname;
    private String lastname;
    private String patronymic;
    private String login;
    private String password;
    private String token;

    public User(int id, String firstname, String lastname, String patronymic, String login, String password) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.patronymic = patronymic;
        this.login = login;
        this.password = password;
    }

    public User() {
    }

    public User(String firstname, String lastname, String patronymic, String login, String password) {
        this(0, firstname, lastname, patronymic, login, password);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void updateEntity(String firstname, String lastname, String patronymic, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.patronymic = patronymic;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() &&
                Objects.equals(getFirstname(), user.getFirstname()) &&
                Objects.equals(getLastname(), user.getLastname()) &&
                Objects.equals(getPatronymic(), user.getPatronymic()) &&
                Objects.equals(getLogin(), user.getLogin()) &&
                Objects.equals(getPassword(), user.getPassword());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getFirstname(), getLastname(), getPatronymic(), getLogin(), getPassword());
    }
}
