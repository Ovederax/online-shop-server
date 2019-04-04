package net.thumbtack.onlineshop.dto.request.cathegory;

public class CategoryEditRequest {
    private String name;
    private Integer parentId;

    public CategoryEditRequest(String name, Integer parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public CategoryEditRequest() {
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
