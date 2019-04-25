package net.thumbtack.onlineshop.dto.request.product;

import net.thumbtack.onlineshop.model.exeptions.enums.ValidationError;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public class ProductAddRequest {
    @NotNull(message = ValidationError.PRODUCT_NAME_CANNOT_BE_NULL)
    private String name;

    @Positive(message = ValidationError.PRODUCT_PRICE_MUST_GREAT_ZERO)
    private int price;

    @PositiveOrZero(message = ValidationError.PRODUCT_COUNT_CANNOT_BE_LESS_ZERO)
    private int count;

    //TODO может сделать проверку на входящие значения?
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
