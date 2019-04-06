package net.thumbtack.onlineshop.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Category {
    private int id;
    private String name;
    private Category parent;
    private List<Category> subCategories;
    private List<Product> products;

    public Category(String name, Category parent, List<Category> subCategories, List<Product> products) {
        this.name = name;
        this.parent = parent;
        this.subCategories = subCategories;
        this.products = products;
    }

    public Category(String name, Category parent) {
        this(name, parent, null, null);
    }

    public Category() { }

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

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> goods) {
        this.products = goods;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Category> subCategories) {
        this.subCategories = subCategories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return getId() == category.getId() &&
                Objects.equals(getName(), category.getName()) &&
                Objects.equals(getParent(), category.getParent()) &&
                Objects.equals(getSubCategories(), category.getSubCategories()) &&
                Objects.equals(getProducts(), category.getProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getParent(), getSubCategories(), getProducts());
    }
}
