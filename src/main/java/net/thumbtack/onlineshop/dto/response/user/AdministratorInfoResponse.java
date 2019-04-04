package net.thumbtack.onlineshop.dto.response.user;

import java.util.Objects;

public class AdministratorInfoResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String position;

    public AdministratorInfoResponse(int id, String firstName, String lastName, String patronymic, String position) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.position = position;
    }

    public AdministratorInfoResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdministratorInfoResponse)) return false;
        AdministratorInfoResponse response = (AdministratorInfoResponse) o;
        return getId() == response.getId() &&
                Objects.equals(getFirstName(), response.getFirstName()) &&
                Objects.equals(getLastName(), response.getLastName()) &&
                Objects.equals(getPatronymic(), response.getPatronymic()) &&
                Objects.equals(getPosition(), response.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getPatronymic(), getPosition());
    }
}
