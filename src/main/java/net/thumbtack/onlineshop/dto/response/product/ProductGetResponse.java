package net.thumbtack.onlineshop.dto.response.product;

import java.util.List;

public class ProductGetResponse {
    private int id;
    private String name;
    private int price;
    private int count;
    private List<String> categoriesNames;

    public ProductGetResponse(int id, String name, int price, int count, List<String> categoriesNames) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.categoriesNames = categoriesNames;
    }

    public ProductGetResponse() {
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
}
