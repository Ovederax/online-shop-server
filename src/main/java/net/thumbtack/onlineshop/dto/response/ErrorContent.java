package net.thumbtack.onlineshop.dto.response;

//example
//{
//    "errors" : [
//        {
//        "errorCode": "LOGIN_ALREADY_EXISTS" ,
//        "field": "login",
//        "message": "User Ivanov already exists"
//        }
//    ]
//}

import java.util.Objects;

public class ErrorContent {
    private String errorCode;
    private String field;
    private String message;

    public ErrorContent(String errorCode, String field, String message) {
        this.errorCode = errorCode;
        this.field = field;
        this.message = message;
    }

    public ErrorContent() {
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorContent that = (ErrorContent) o;
        return Objects.equals(errorCode, that.errorCode) &&
                Objects.equals(field, that.field) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {

        return Objects.hash(errorCode, field, message);
    }
}
