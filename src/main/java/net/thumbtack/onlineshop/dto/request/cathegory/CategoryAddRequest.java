package net.thumbtack.onlineshop.dto.request.cathegory;

public class CategoryAddRequest {
    private String name;
    private String parentId;

    public CategoryAddRequest(String name, String parentId) {
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

    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
