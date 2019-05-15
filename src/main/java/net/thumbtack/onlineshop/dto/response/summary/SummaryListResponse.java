package net.thumbtack.onlineshop.dto.response.summary;

import java.util.List;
import java.util.Objects;

public class SummaryListResponse {
    List<SummaryListByCategory> summaryListByCategories;
    List<SummaryListByProduct> summaryListByProducts;
    List<SummaryListByClient> summaryListByClients;

    public SummaryListResponse(List<SummaryListByCategory> summaryListByCategories, List<SummaryListByProduct> summaryListByProducts, List<SummaryListByClient> summaryListByClients) {
        this.summaryListByCategories = summaryListByCategories;
        this.summaryListByProducts = summaryListByProducts;
        this.summaryListByClients = summaryListByClients;
    }

    public SummaryListResponse() {
    }

    public List<SummaryListByCategory> getSummaryListByCategories() {
        return summaryListByCategories;
    }

    public void setSummaryListByCategories(List<SummaryListByCategory> summaryListByCategories) {
        this.summaryListByCategories = summaryListByCategories;
    }

    public List<SummaryListByProduct> getSummaryListByProducts() {
        return summaryListByProducts;
    }

    public void setSummaryListByProducts(List<SummaryListByProduct> summaryListByProducts) {
        this.summaryListByProducts = summaryListByProducts;
    }

    public List<SummaryListByClient> getSummaryListByClients() {
        return summaryListByClients;
    }

    public void setSummaryListByClients(List<SummaryListByClient> summaryListByClients) {
        this.summaryListByClients = summaryListByClients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SummaryListResponse that = (SummaryListResponse) o;
        return Objects.equals(summaryListByCategories, that.summaryListByCategories) &&
                Objects.equals(summaryListByProducts, that.summaryListByProducts) &&
                Objects.equals(summaryListByClients, that.summaryListByClients);
    }

    @Override
    public int hashCode() {

        return Objects.hash(summaryListByCategories, summaryListByProducts, summaryListByClients);
    }
}
