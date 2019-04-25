package net.thumbtack.onlineshop.model.entity;

// item from sql table baskets
public class BasketItem {
    private int id;
    private Product product;
    private int count;

    public BasketItem(Product product, int count) {
        this.product = product;
        this.count = count;
    }

    public BasketItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
