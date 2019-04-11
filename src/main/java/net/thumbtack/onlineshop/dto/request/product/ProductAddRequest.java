package net.thumbtack.onlineshop.dto.request.product;

import java.util.List;

public class ProductAddRequest {
    private String name;
    private int price;
    private int count;
    private List<Integer> categories;

    public ProductAddRequest(String name, int price, int count, List<Integer> categories) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.categories = categories;
    }

    public ProductAddRequest() {
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

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }
}
