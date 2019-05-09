package net.thumbtack.onlineshop.model.transfer;

import net.thumbtack.onlineshop.model.entity.BasketItem;
import net.thumbtack.onlineshop.model.entity.Purchase;

public class PurchaseBuyInfo {
    private Purchase purchase;
    private BasketItem basketItem;
    private int newProductCount;

    public PurchaseBuyInfo(Purchase purchase, BasketItem basketItem, int newProductCount) {
        this.purchase = purchase;
        this.basketItem = basketItem;
        this.newProductCount = newProductCount;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public BasketItem getBasketItem() {
        return basketItem;
    }

    public void setBasketItem(BasketItem basketItem) {
        this.basketItem = basketItem;
    }

    public int getNewProductCount() {
        return newProductCount;
    }

    public void setNewProductCount(int newProductCount) {
        this.newProductCount = newProductCount;
    }
}
