package net.thumbtack.onlineshop.model.exeptions.enums;



public enum ProductExceptionEnum {
    //VALIDATE ERROR
    //BAD_FIRST_NAME  ("firstname",   "Your firsname is not correct"),

    //DATABASE ERROR
    //LOGIN_ALREADY_EXISTS("login",   "This login is used")

    ;

    private String message;
    private String field;

    ProductExceptionEnum(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getField() {
        return field;
    }

    public String getErrorCode() {
        return toString();
    }
}
