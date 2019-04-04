package net.thumbtack.onlineshop.model.exeptions;

import net.thumbtack.onlineshop.model.exeptions.enums.BasketExceptionEnum;


public class BasketException extends ServerException {
    private BasketExceptionEnum exEnum;

    public BasketException(BasketExceptionEnum exEnum) {
        this.exEnum = exEnum;
    }
    public BasketExceptionEnum getExEnum() {
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
