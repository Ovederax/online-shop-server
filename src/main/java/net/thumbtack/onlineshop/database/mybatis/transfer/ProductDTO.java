package net.thumbtack.onlineshop.database.mybatis.transfer;

import java.util.List;
import java.util.Objects;

public class ProductDTO {
    private int id;
    private String name;
    private Integer price;
    private Integer count;
    private List<Integer> categories;

    public ProductDTO(int id, String name, Integer price, Integer count, List<Integer> categories) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.categories = categories;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
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
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO that = (ProductDTO) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(price, that.price) &&
                Objects.equals(count, that.count) &&
                Objects.equals(categories, that.categories);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, price, count, categories);
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", categories=" + categories +
                '}';
    }
}
