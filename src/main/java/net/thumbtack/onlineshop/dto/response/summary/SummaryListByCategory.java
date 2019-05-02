package net.thumbtack.onlineshop.dto.response.summary;

import net.thumbtack.onlineshop.dto.response.category.GetCategoryResponse;
import net.thumbtack.onlineshop.dto.response.purchase.PurchaseResponse;
import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.entity.Purchase;

import java.util.List;
import java.util.Objects;

public class SummaryListByCategory {
    private GetCategoryResponse category;
    private List<PurchaseResponse> purchases;


    public SummaryListByCategory(GetCategoryResponse category, List<PurchaseResponse> purchases) {
        this.category = category;
        this.purchases = purchases;
    }

    public SummaryListByCategory() {
    }

    public GetCategoryResponse getCategory() {
        return category;
    }

    public void setCategory(GetCategoryResponse category) {
        this.category = category;
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
        SummaryListByCategory that = (SummaryListByCategory) o;
        return Objects.equals(category, that.category) &&
                Objects.equals(purchases, that.purchases);
    }

    @Override
    public int hashCode() {

        return Objects.hash(category, purchases);
    }
}
