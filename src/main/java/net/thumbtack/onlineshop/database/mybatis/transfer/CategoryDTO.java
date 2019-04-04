package net.thumbtack.onlineshop.database.mybatis.transfer;

public class CategoryDTO {
    private int id;
    private String name;
    private String parentId;

    public CategoryDTO(int id, String name, String parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public CategoryDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
