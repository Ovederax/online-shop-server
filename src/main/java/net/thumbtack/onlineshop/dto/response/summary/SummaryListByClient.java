package net.thumbtack.onlineshop.dto.response.summary;

import net.thumbtack.onlineshop.dto.response.purchase.PurchaseResponse;
import net.thumbtack.onlineshop.dto.response.user.ClientInfo;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.Purchase;

import java.util.List;
import java.util.Objects;

public class SummaryListByClient {
    private ClientInfo clientInfo;
    private List<PurchaseResponse> purchases;
    private int summaryAmount;

    public SummaryListByClient(ClientInfo clientInfo, List<PurchaseResponse> purchases, int summaryAmount) {
        this.clientInfo = clientInfo;
        this.purchases = purchases;
        this.summaryAmount = summaryAmount;
    }

    public SummaryListByClient() {
    }


    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public List<PurchaseResponse> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchaseResponse> purchases) {
        this.purchases = purchases;
    }

    public int getSummaryAmount() {
        return summaryAmount;
    }

    public void setSummaryAmount(int summaryAmount) {
        this.summaryAmount = summaryAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SummaryListByClient that = (SummaryListByClient) o;
        return summaryAmount == that.summaryAmount &&
                Objects.equals(clientInfo, that.clientInfo) &&
                Objects.equals(purchases, that.purchases);
    }

    @Override
    public int hashCode() {

        return Objects.hash(clientInfo, purchases, summaryAmount);
    }
}
