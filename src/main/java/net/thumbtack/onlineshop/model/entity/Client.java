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


public class Client {
    private int id;
    private String firstname;
    private String lastname;
    private String patronymic;
    private String email;
    private String address;
    private String telefon;
    private int deposit;
    private String login;
    private String password;

    public Client(String firstname, String lastname, String patronymic, String email, String address, String telefon, String login, String password) {
        this.id = 0;
        this.deposit = 0;
        this.firstname = firstname;
        this.lastname = lastname;
        this.patronymic = patronymic;
        this.email = email;
        this.address = address;
        this.telefon = telefon;
        this.login = login;
        this.password = password;
    }

    public Client() {
    }

    public int getDeposit() {
        return deposit;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public void setDeposit(int deposit) {
        this.deposit = deposit;
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

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
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
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return Objects.equals(getFirstname(), client.getFirstname()) &&
                Objects.equals(getLastname(), client.getLastname()) &&
                Objects.equals(getPatronymic(), client.getPatronymic()) &&
                Objects.equals(getEmail(), client.getEmail()) &&
                Objects.equals(getAddress(), client.getAddress()) &&
                Objects.equals(getTelefon(), client.getTelefon()) &&
                Objects.equals(getLogin(), client.getLogin()) &&
                Objects.equals(getPassword(), client.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstname(), getLastname(), getPatronymic(), getEmail(), getAddress(), getTelefon(), getLogin(), getPassword());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
