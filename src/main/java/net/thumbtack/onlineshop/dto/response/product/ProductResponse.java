package net.thumbtack.onlineshop.dto.response.product;

import java.util.List;
import java.util.Objects;

public class ProductResponse {
    private int id;
    private String name;
    private long price;
    private long count;
    private List<Integer> categories;

    public ProductResponse(int id, String name, long price, long count, List<Integer> categories) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.categories = categories;
    }

    public ProductResponse() {
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductResponse)) return false;
        ProductResponse that = (ProductResponse) o;
        return getId() == that.getId() &&
                getPrice() == that.getPrice() &&
                getCount() == that.getCount() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getCategories(), that.getCategories());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getName(), getPrice(), getCount(), getCategories());
    }
}
