package net.thumbtack.onlineshop.dto.response.user;

import java.util.Objects;

public class ClientInfo {
    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;
    private String address;
    private String phone;
    private String userType; // "client"

    public ClientInfo(int id, String firstName, String lastName, String patronymic, String email, String address, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.userType = "client";
    }

    public ClientInfo() {
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientInfo that = (ClientInfo) o;
        return id == that.id &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(patronymic, that.patronymic) &&
                Objects.equals(email, that.email) &&
                Objects.equals(address, that.address) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(userType, that.userType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstName, lastName, patronymic, email, address, phone, userType);
    }
}
