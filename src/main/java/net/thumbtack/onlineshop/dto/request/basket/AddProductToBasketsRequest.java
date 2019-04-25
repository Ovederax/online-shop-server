package net.thumbtack.onlineshop.dto.request.basket;

import net.thumbtack.onlineshop.model.exeptions.enums.ValidationError;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Objects;


public class AddProductToBasketsRequest {
    @Min(value = 1, message = ValidationError.ID_MUST_GREAT_ZERO)
    private int id;

    @NotNull(message = ValidationError.PRODUCT_NAME_CANNOT_BE_NULL)
    private String name;

    @Min(value = 0, message = ValidationError.PRODUCT_PRICE_MUST_GREAT_ZERO)
    private int price;

    @PositiveOrZero(message = ValidationError.PRODUCT_COUNT_CANNOT_BE_LESS_ZERO)
    private int count;   // необязателен

    public AddProductToBasketsRequest(int productId, String name, int price, int count) {
        this.id = productId;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public AddProductToBasketsRequest() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddProductToBasketsRequest that = (AddProductToBasketsRequest) o;
        return id == that.id &&
                price == that.price &&
                count == that.count &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, price, count);
    }
}
