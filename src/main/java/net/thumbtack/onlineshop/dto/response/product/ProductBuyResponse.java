package net.thumbtack.onlineshop.dto.response.product;

import java.util.Objects;

public class ProductBuyResponse {
    private int id;
    private String name;
    private int price;
    private int count;



    public ProductBuyResponse() {
    }

    public ProductBuyResponse(int id, String name, int price, int count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
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
        if (!(o instanceof ProductBuyResponse)) return false;
        ProductBuyResponse that = (ProductBuyResponse) o;
        return getId() == that.getId() &&
                getPrice() == that.getPrice() &&
                getCount() == that.getCount() &&
                Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getName(), getPrice(), getCount());
    }
}
