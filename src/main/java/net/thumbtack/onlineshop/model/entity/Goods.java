package net.thumbtack.onlineshop.model.entity;

import java.util.List;

// REVU I do not like this name
// Goods - plural, so misunderstanding can be
// Product is better
public class Goods {
    private int id;
    private String name;
    private int price;
    private int count;
    private List<Category> categories;

    public Goods(String name, int price, int count, List<Category> categories) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.categories = categories;
    }

    public Goods() {
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
}
