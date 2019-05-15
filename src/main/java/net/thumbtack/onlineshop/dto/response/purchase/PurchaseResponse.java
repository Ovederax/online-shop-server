package net.thumbtack.onlineshop.dto.response.purchase;

import net.thumbtack.onlineshop.dto.response.product.GetProductResponse;
import java.util.Objects;

public class PurchaseResponse {
    private int id;
    private GetProductResponse product;
    private String name;
    private int buyPrice;
    private int buyCount;


    public PurchaseResponse(int id, GetProductResponse product, String name, int buyPrice, int buyCount) {
        this.id = id;
        this.product = product;
        this.name = name;
        this.buyCount = buyCount;
        this.buyPrice = buyPrice;
    }
    public PurchaseResponse(int id, String name, int buyPrice, int buyCount) {
        this(id, null, name, buyPrice, buyCount);
    }

    public PurchaseResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GetProductResponse getProduct() {
        return product;
    }

    public void setProduct(GetProductResponse product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseResponse that = (PurchaseResponse) o;
        return id == that.id &&
                buyCount == that.buyCount &&
                buyPrice == that.buyPrice &&
                Objects.equals(product, that.product) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, product, name, buyCount, buyPrice);
    }
}
