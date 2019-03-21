package net.thumbtack.onlineshop.model.entity;

import java.util.Objects;

public class Administrator {
    private int id;
    private String firstname;
    private String lastname;
    private String patronymic;
    private String position;
    private String login;
    private String password;

    public Administrator(String firstname, String lastname, String patronymic, String position, String login, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.patronymic = patronymic;
        this.position = position;
        this.login = login;
        this.password = password;
    }

    public Administrator() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Administrator)) return false;
        Administrator that = (Administrator) o;
        return Objects.equals(getFirstname(), that.getFirstname()) &&
                Objects.equals(getLastname(), that.getLastname()) &&
                Objects.equals(getPatronymic(), that.getPatronymic()) &&
                Objects.equals(getPosition(), that.getPosition()) &&
                Objects.equals(getLogin(), that.getLogin()) &&
                Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstname(), getLastname(), getPatronymic(), getPosition(), getLogin(), getPassword());
    }
}
