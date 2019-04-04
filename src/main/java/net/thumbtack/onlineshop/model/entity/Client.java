package net.thumbtack.onlineshop.model.entity;

import java.util.Objects;

//Для того, чтобы купить товар, зарегистрированный клиент должен
//сначала положить некоторую сумму денег (рублей) на свой счет.
//Счет создается для каждого клиента автоматически при регистрации.
//Зарегистрированный клиент может купить любой товар в любом
//количестве, если он имеется в наличии. Если клиент принимает
//решение купить товар, то он условно оплачивает его.
//Оплата товара производится только со счета клиента.
//Альтернативно, клиент может добавить товар в свою Корзину клиента.
//В дальнейшем он может удалить этот товар из корзины или купить
//все или некоторые товары, находящиеся в Корзине.
//Товары, находящиеся в Корзине, не считаются купленными
//клиентом, поэтому не исключена ситуация, что товар имелся в
//наличии в момент его добавления в Корзину, а в момент покупки
//этого товара уже нет либо в нужном количестве, либо вообще.


public class Client extends User{
    private String email;
    private String address;
    private String phone;
    private int deposit;

    public Client(String firstname, String lastname, String patronymic, String email, String address, String phone, String login, String password) {
        super(0, firstname, lastname, patronymic, login, password);
        this.deposit = 0;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public Client() {
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
    public int hashCode() {
        return Objects.hash(getEmail(), getAddress(), getPhone(), getDeposit());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        if (!super.equals(o)) return false;
        Client client = (Client) o;
        return getDeposit() == client.getDeposit() &&
                Objects.equals(getEmail(), client.getEmail()) &&
                Objects.equals(getAddress(), client.getAddress()) &&
                Objects.equals(getPhone(), client.getPhone());
    }
}
