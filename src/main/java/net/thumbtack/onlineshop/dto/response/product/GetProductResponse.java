package net.thumbtack.onlineshop.dto.response.product;

import java.util.List;
import java.util.Objects;

public class GetProductResponse {
    private int id;
    private String name;
    private int price;
    private int count;
    private List<String> categoriesNames;

    public GetProductResponse(int id, String name, int price, int count, List<String> categoriesNames) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.categoriesNames = categoriesNames;
    }

    public GetProductResponse() {
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

    public List<String> getCategoriesNames() {
        return categoriesNames;
    }

    public void setCategoriesNames(List<String> categoriesNames) {
        this.categoriesNames = categoriesNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetProductResponse)) return false;
        GetProductResponse that = (GetProductResponse) o;
        return getId() == that.getId() &&
                getPrice() == that.getPrice() &&
                getCount() == that.getCount() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getCategoriesNames(), that.getCategoriesNames());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getName(), getPrice(), getCount(), getCategoriesNames());
    }
}
