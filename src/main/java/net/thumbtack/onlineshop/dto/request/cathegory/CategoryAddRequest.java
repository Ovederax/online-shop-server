// REVU package net.thumbtack.onlineshop.dto.request.category;
package net.thumbtack.onlineshop.dto.request.cathegory;

import net.thumbtack.onlineshop.model.exeptions.enums.ValidationError;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class CategoryAddRequest {
    @NotNull(message = ValidationError.CATEGORY_NAME_CANNOT_BE_NULL)
    private String name;
    //@Positive(message = ValidationError.PARENT_ID_MUST_GREAT_ZERO)
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
