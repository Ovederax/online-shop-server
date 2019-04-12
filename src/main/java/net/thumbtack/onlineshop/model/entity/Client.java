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
    private Deposit deposit;

    public Client(String firstname, String lastname, String patronymic, String email, String address, String phone, String login, String password) {
        super(0, firstname, lastname, patronymic, login, password);
        this.deposit = null;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public Client() {
    }

    public void updateEntity(String firstname, String lastname, String patronymic, String email, String address, String phone, String password) {
        // REVU do not set fields of parent class in this class
    	// use super(...) or this(...)
    	setFirstname(firstname);
        setLastname(lastname);
        setPatronymic(patronymic);
        setPassword(password);
        this.email = email;
        this.address = address;
        this.phone = phone;
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

    public Deposit getDeposit() {
        return deposit;
    }

    public void setDeposit(Deposit deposit) {
        this.deposit = deposit;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Client client = (Client) o;
        return Objects.equals(email, client.email) &&
                Objects.equals(address, client.address) &&
                Objects.equals(phone, client.phone) &&
                Objects.equals(deposit, client.deposit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, address, phone, deposit);
    }
}
