package net.thumbtack.onlineshop.dto.response.product;

import java.util.Objects;

public class ProductBuyResponse {
    private String id;
    private String name;
    private String price;
    private String count;

    public ProductBuyResponse(String id, String name, String price, String count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public ProductBuyResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductBuyResponse)) return false;
        ProductBuyResponse that = (ProductBuyResponse) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getPrice(), that.getPrice()) &&
                Objects.equals(getCount(), that.getCount());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getName(), getPrice(), getCount());
    }
}
