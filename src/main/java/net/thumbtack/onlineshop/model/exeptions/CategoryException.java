package net.thumbtack.onlineshop.model.exeptions;

import net.thumbtack.onlineshop.model.exeptions.enums.CategoryExceptionEnum;

public class CategoryException extends ServerException {
    private CategoryExceptionEnum exEnum;

    public CategoryException(CategoryExceptionEnum exEnum) {
        this.exEnum = exEnum;
    }
    public CategoryExceptionEnum getExEnum() {
        return exEnum;
    }

    @Override
    public String getErrorCode() {
        return exEnum.getErrorCode();
    }

    @Override
    public String getField() {
        return exEnum.getField();
    }

    @Override
    public String getMessage() {
        return exEnum.getMessage();
    }
}
