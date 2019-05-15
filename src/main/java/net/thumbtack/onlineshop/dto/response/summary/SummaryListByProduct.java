package net.thumbtack.onlineshop.dto.response.summary;

import net.thumbtack.onlineshop.dto.response.product.GetProductResponse;
import net.thumbtack.onlineshop.dto.response.purchase.PurchaseResponse;

import java.util.List;
import java.util.Objects;

public class SummaryListByProduct {
    private GetProductResponse product;
    private List<PurchaseResponse> purchases;

    public SummaryListByProduct(GetProductResponse product, List<PurchaseResponse> purchases) {
        this.product = product;
        this.purchases = purchases;
    }

    public SummaryListByProduct() {
    }

    public GetProductResponse getProduct() {
        return product;
    }

    public void setProduct(GetProductResponse product) {
        this.product = product;
    }

    public List<PurchaseResponse> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchaseResponse> purchases) {
        this.purchases = purchases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SummaryListByProduct that = (SummaryListByProduct) o;
        return Objects.equals(product, that.product) &&
                Objects.equals(purchases, that.purchases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, purchases);
    }
}
