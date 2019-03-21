package net.thumbtack.onlineshop.dto.response.user;

import java.util.Objects;

public class ClientInfoResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;
    private String address;
    private String phone;
    private int deposit;


    public ClientInfoResponse(int id, String firstName, String lastName, String patronymic, String email, String address, String phone, int deposit) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.deposit = deposit;
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

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientInfoResponse)) return false;
        ClientInfoResponse response = (ClientInfoResponse) o;
        return getId() == response.getId() &&
                getDeposit() == response.getDeposit() &&
                Objects.equals(getFirstName(), response.getFirstName()) &&
                Objects.equals(getLastName(), response.getLastName()) &&
                Objects.equals(getPatronymic(), response.getPatronymic()) &&
                Objects.equals(getEmail(), response.getEmail()) &&
                Objects.equals(getAddress(), response.getAddress()) &&
                Objects.equals(getPhone(), response.getPhone());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getFirstName(), getLastName(), getPatronymic(), getEmail(), getAddress(), getPhone(), getDeposit());
    }
}
