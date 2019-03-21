package net.thumbtack.onlineshop.model.exeptions;

import net.thumbtack.onlineshop.model.exeptions.enums.UserExceptionEnum;

public class UserException extends ServerException {
    private UserExceptionEnum exEnum;

    public UserException(UserExceptionEnum exEnum) {
        this.exEnum = exEnum;
    }
    public UserExceptionEnum getExEnum() {
        return exEnum;
    }

    @Override
    public String getMessage() {
        return exEnum.getDescription();
    }
}
