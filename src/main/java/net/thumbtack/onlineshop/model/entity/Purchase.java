package net.thumbtack.onlineshop.model.entity;

import java.util.Objects;

public class Purchase {
    private int id;
    private Product actual;
    private String name;
    private int buyCount;
    private int buyPrice;

    public Purchase(Product actual, String name, int buyCount, int buyPrice) {
        this.actual = actual;
        this.name = name;
        this.buyCount = buyCount;
        this.buyPrice = buyPrice;
    }

    public Purchase() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getActual() {
        return actual;
    }

    public void setActual(Product actual) {
        this.actual = actual;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Purchase)) return false;
        Purchase purchase = (Purchase) o;
        return getId() == purchase.getId() &&
                getBuyCount() == purchase.getBuyCount() &&
                getBuyPrice() == purchase.getBuyPrice() &&
                Objects.equals(getActual(), purchase.getActual()) &&
                Objects.equals(getName(), purchase.getName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getActual(), getName(), getBuyCount(), getBuyPrice());
    }
}
