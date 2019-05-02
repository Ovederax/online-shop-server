package net.thumbtack.onlineshop.dto.response.basket;

import net.thumbtack.onlineshop.dto.response.product.BuyProductResponse;

import java.util.List;
import java.util.Objects;

public class BuyProductFromBasketResponse {
    private List<BuyProductResponse> bought;
    private List<ProductInBasketResponse> remaining;



    public BuyProductFromBasketResponse() {
    }

    public BuyProductFromBasketResponse(List<BuyProductResponse> bought, List<ProductInBasketResponse> remaining) {
        this.bought = bought;
        this.remaining = remaining;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuyProductFromBasketResponse response = (BuyProductFromBasketResponse) o;
        return Objects.equals(bought, response.bought) &&
                Objects.equals(remaining, response.remaining);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bought, remaining);
    }

    public List<BuyProductResponse> getBought() {
        return bought;
    }

    public void setBought(List<BuyProductResponse> bought) {
        this.bought = bought;
    }

    public List<ProductInBasketResponse> getRemaining() {
        return remaining;
    }

    public void setRemaining(List<ProductInBasketResponse> remaining) {
        this.remaining = remaining;
    }
}
