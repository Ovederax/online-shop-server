package net.thumbtack.onlineshop.dto.request.category;

import net.thumbtack.onlineshop.model.exeptions.enums.ValidationError;

import javax.validation.constraints.NotNull;

public class AddCategoryRequest {
    @NotNull(message = ValidationError.CATEGORY_NAME_CANNOT_BE_NULL)
    private String name;

    //@Positive(message = ValidationError.PARENT_ID_MUST_GREAT_ZERO)
    private Integer parentId; // необязателен

    public AddCategoryRequest(String name, Integer parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public AddCategoryRequest() {
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
