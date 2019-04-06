package net.thumbtack.onlineshop.model.exeptions;

import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;

public class ServerException extends Exception {
    private ErrorCode exEnum;

    public ServerException(ErrorCode exEnum) {
        this.exEnum = exEnum;
    }
    public ErrorCode getExEnum() {
        return exEnum;
    }

    public String getErrorCode() {
        return exEnum.getErrorCode();
    }

    public String getField() {
        return exEnum.getField();
    }

    public String getMessage() {
        return exEnum.getMessage();
    }
}
