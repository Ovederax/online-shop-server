package net.thumbtack.onlineshop.model.exeptions;

import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;

public class ServerException extends Exception {
    private String error_code;
    private String message;
    private String field;

    public ServerException(String error_code, String message, String field) {
        this.error_code = error_code;
        this.message = message;
        this.field = field;
    }

    public ServerException(ErrorCode exEnum) {
        this(exEnum.getErrorCode(), exEnum.getMessage(), exEnum.getField());
    }


    public String getErrorCode() {
        return error_code;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
