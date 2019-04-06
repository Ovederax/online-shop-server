package net.thumbtack.onlineshop.model.entity;

import java.util.List;
import java.util.Objects;

public class Product {
    private int id;
    private String name;
    private int price;
    private int count;
    private int isDeleted;
    private List<Category> categories;

    public Product(int id, String name, int price, int count, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.isDeleted = 0;
        this.categories = categories;
    }

    public Product(String name, int price, int count, List<Category> categories) {
        this(0, name, price, count, categories);
    }

    public Product() {
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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", isDeleted=" + isDeleted +
                ", categories=" + categories +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id &&
                price == product.price &&
                count == product.count &&
                isDeleted == product.isDeleted &&
                Objects.equals(name, product.name) &&
                Objects.equals(categories, product.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, count, isDeleted, categories);
    }

    /**Update field if arguments is not null*/
    public void updateEntity(String name, Integer price, Integer count) {
        if(name != null)
            this.name = name;
        if(price != null)
            this.price = price;
        if(count != null)
            this.count = count;
    }
}
