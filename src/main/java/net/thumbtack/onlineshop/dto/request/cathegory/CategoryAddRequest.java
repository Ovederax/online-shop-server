package net.thumbtack.onlineshop.dto.request.cathegory;

public class CategoryAddRequest {
    private String name;
    private Integer parentId; // необязателен

    public CategoryAddRequest(String name, Integer parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public CategoryAddRequest() {
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
