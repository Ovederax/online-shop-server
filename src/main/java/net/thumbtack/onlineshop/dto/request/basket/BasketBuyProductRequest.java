package net.thumbtack.onlineshop.dto.request.basket;

import java.util.Objects;

public class BasketBuyProductRequest {
    private int id;
    private String name;
    private int price;
    private int count; // необязателен

    public BasketBuyProductRequest(int id, String name, int price, Integer count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public BasketBuyProductRequest() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasketBuyProductRequest that = (BasketBuyProductRequest) o;
        return id == that.id &&
                price == that.price &&
                count == that.count &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, price, count);
    }

    @Override
    public String toString() {
        return "BasketBuyProductRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                '}';
    }
}