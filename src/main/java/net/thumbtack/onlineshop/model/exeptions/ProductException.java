package net.thumbtack.onlineshop.model.exeptions;

import net.thumbtack.onlineshop.model.exeptions.enums.CategoryExceptionEnum;
import net.thumbtack.onlineshop.model.exeptions.enums.ProductExceptionEnum;

public class ProductException extends ServerException {
    private ProductExceptionEnum exEnum;

    public ProductException(ProductExceptionEnum exEnum) {
        this.exEnum = exEnum;
    }
    public ProductExceptionEnum getExEnum() {
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
