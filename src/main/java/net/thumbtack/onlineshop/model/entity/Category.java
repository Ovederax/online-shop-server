package net.thumbtack.onlineshop.model.entity;

import java.util.List;

public class Category {
    private int id;
    private String name;
    private Category parent;
    // REVU what about private List<Category> subCategories ?

    private List<Goods> goods;

    public Category(String name, Category parent, List<Goods> goods) {
        this.name = name;
        this.parent = parent;
        this.goods = goods;
    }

    public Category() {
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

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }
}
