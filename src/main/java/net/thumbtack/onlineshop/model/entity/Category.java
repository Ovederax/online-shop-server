package net.thumbtack.onlineshop.model.entity;

import java.util.ArrayList;
import java.util.List;

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
}
