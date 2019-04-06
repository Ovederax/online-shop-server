package net.thumbtack.onlineshop.dto.response.user;

import java.util.Objects;

public class ClientInfoResponse extends UserInfoResponse{
    private String email;
    private String address;
    private String phone;
    private int deposit;


    public ClientInfoResponse(int id, String firstName, String lastName, String patronymic, String email, String address, String phone, int deposit) {
        super(id, firstName, lastName, patronymic);
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.deposit = deposit;
    }

    public ClientInfoResponse() {
        super();
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
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ClientInfoResponse that = (ClientInfoResponse) o;
        return deposit == that.deposit &&
                Objects.equals(email, that.email) &&
                Objects.equals(address, that.address) &&
                Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, address, phone, deposit);
    }
}
