package net.thumbtack.onlineshop.dto.request.product;

import java.util.List;

public class ProductEditRequest {
    private String name;
    private Integer price;
    private Integer count;
    private List<Integer> categories;


    public ProductEditRequest() {
    }


    public ProductEditRequest(String name, Integer price, Integer count, List<Integer> categories) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.categories = categories;
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
}
