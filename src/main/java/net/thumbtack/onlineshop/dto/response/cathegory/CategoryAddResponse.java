// REVU net.thumbtack.onlineshop.dto.response.category;
package net.thumbtack.onlineshop.dto.response.cathegory;

import java.util.Objects;

public class CategoryAddResponse {
    private int id;
    private String name;
    private int parentId;
    private String parentName;

    public CategoryAddResponse(int id, String name, int parentId, String parentName) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.parentName = parentName;
    }

    public CategoryAddResponse() {
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getParentName() {
        return parentName;
    }
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryAddResponse)) return false;
        CategoryAddResponse that = (CategoryAddResponse) o;
        return getId() == that.getId() &&
                getParentId() == that.getParentId() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getParentName(), that.getParentName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getName(), getParentId(), getParentName());
    }
}
