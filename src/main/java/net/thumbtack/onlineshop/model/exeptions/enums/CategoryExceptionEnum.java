package net.thumbtack.onlineshop.model.exeptions.enums;


public enum CategoryExceptionEnum {
    //VALIDATE ERROR
    //BAD_FIRST_NAME  ("firstname",   "Your firsname is not correct"),

    //DATABASE ERROR
    CATEGORY_NO_EXISTS("id",   "This category is no exist"),

    BAD_SET_SUBCATEGORY_INTO_SUBCATEGORY("parentId", "You no can set subcategory into subcategory");

    private String message;
    private String field;

    CategoryExceptionEnum(String field, String message) {
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
