package net.thumbtack.onlineshop.model.entity;

import java.util.Objects;

public class Deposit {
    private int id;
    private int clientId;
    private int money;

    public Deposit(int clientId, int money) {
        this.clientId = clientId;
        this.money = money;
    }

    public Deposit() { }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deposit deposit = (Deposit) o;
        return id == deposit.id &&
                clientId == deposit.clientId &&
                money == deposit.money;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, clientId, money);
    }

    @Override
    public String toString() {
        return "Deposit{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", money=" + money +
                '}';
    }
}
