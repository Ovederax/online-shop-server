package net.thumbtack.onlineshop.dto.response.basket;

import net.thumbtack.onlineshop.dto.response.product.ProductBuyResponse;
import net.thumbtack.onlineshop.model.entity.BasketItem;

import java.util.List;
import java.util.Objects;

public class BasketBuyProductResponse {
    private List<ProductBuyResponse> bought;
    private List<ProductInBasketResponse> remaining;



    public BasketBuyProductResponse() {
    }

    public BasketBuyProductResponse(List<ProductBuyResponse> bought, List<ProductInBasketResponse> remaining) {
        this.bought = bought;
        this.remaining = remaining;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasketBuyProductResponse response = (BasketBuyProductResponse) o;
        return Objects.equals(bought, response.bought) &&
                Objects.equals(remaining, response.remaining);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bought, remaining);
    }

    public List<ProductBuyResponse> getBought() {
        return bought;
    }

    public void setBought(List<ProductBuyResponse> bought) {
        this.bought = bought;
    }

    public List<ProductInBasketResponse> getRemaining() {
        return remaining;
    }

    public void setRemaining(List<ProductInBasketResponse> remaining) {
        this.remaining = remaining;
    }
}
