package net.thumbtack.onlineshop.dto.request.product;

import net.thumbtack.onlineshop.model.exeptions.enums.ValidationError;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public class EditProductRequest {
    @NotNull(message = ValidationError.PRODUCT_NAME_CANNOT_BE_NULL)
    private String name;

    @Positive(message = ValidationError.PRODUCT_PRICE_MUST_GREAT_ZERO)
    private Integer price;

    @PositiveOrZero(message = ValidationError.PRODUCT_COUNT_CANNOT_BE_LESS_ZERO)
    private Integer count;

    private List<Integer> categories;


    public EditProductRequest() {
    }


    public EditProductRequest(String name, Integer price, Integer count, List<Integer> categories) {
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
