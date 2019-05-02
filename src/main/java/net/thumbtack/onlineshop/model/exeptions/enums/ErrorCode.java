package net.thumbtack.onlineshop.model.exeptions.enums;


public enum ErrorCode {
    //VALIDATE ERROR
    BAD_FIRST_NAME  ("firstname",   "Your firsname is not correct"),
    BAD_LAST_NAME   ("lastname",    "Your lastname is not correct"),
    BAD_LOGIN       ("login",       "Your login is bad"),
    BAD_PASSWORD    ("password",    "Your password is bad"),
    BAD_POSITION    ("position",    "Position is not correct"),
    VALIDATE_ERROR("", ""),

    //DATABASE ERROR
    LOGIN_ALREADY_EXISTS("login",   "This login is used"),
    LOGIN_NOT_FOUND_DB  ("login",   "This login is not found"),
    USER_IS_INACTIVE    ("token",   "User is inactive now, no need logout account"),
    USER_IS_ACTIVE      ("token",   "User is active now, no need login account"),
    USER_IS_NOT_ADMIN   ("token",   "User is not administrator"),
    UUID_NOT_FOUND              ("token",    "Your uuid is not found"),
    EDIT_NOT_YOUR_TYPE_USER     ("token",    "You edit not your type of user"),
    YOU_NO_HAVE_THIS_PRIVILEGES ("token",    "You no have this privileges"),
    USER_IS_NOT_CLIENT("token", "User is not client" ),
    YOU_ARE_NOT_LOGIN("token", "You are not login"),

    CATEGORY_NO_EXISTS("id",   "This category is no exist"),
    BAD_SET_SUBCATEGORY_INTO_SUBCATEGORY("parentId", "You no can set subcategory into subcategory"),
    UPDATE_PRODUCT_SET_CATEGORIES_NO_SUPPORT("categories", "You no can set categories in product where update it"),
    BAD_ORDER_FOR_GET_PROGUCT_LIST("order", "Bad order for get products list"),
    BAD_UPDATE_PRODUCT_IN_BASKET("", "Bad update product in basket"),
    NO_BUY_IF_PRODUCT_IS_CHANGE("field", "No make buy if product is changed"),
    YOU_NEED_MORE_MONEY_TO_BUY("money", "You need more money to buy product"),
    BUY_COUNT_PRODUCT_LESS_NEED_COUNT("count", "You no can buy product with this count"),
    NO_BUY_IF_CLIENT_DEPOSIT_IS_CHANGE("money", "You no can buy product if your money deposit is change"),
    BAD_UPDATE_DEPOSIT_IT_IS_CHANGE("money", "Bad update deposit, deposit is change"),
    PRODUCT_IS_DELETED("product", "Product is deleted"),
    CANT_ADD_PRODUCT_WITH_NO_UNIQUE_NAME("name", "Cant add product with no unique name"),
    CANT_ADD_CATEGORY_WITH_NO_UNIQUE_NAME("name", "Cant add category with no unique name"),

    //-----DATABASE COMMON_ERROR---------------

    DB_CANT_DELETE_FROM_BASKET("", ""),

    NOT_FOUND("","Page not found"),
    INTERNAL_SERVER_ERROR("", "Internal server error"),

    //-----------REQUEST ERROR-------------
    NULL_REQUEST("", "No data in Request"),
    COOKIE_MISSING("", "Cookie is missing");

    private String message;
    private String field;
    // private String type; ("user" | "category" | "user category product") in future maybe added

    ErrorCode(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getErrorCode() {
        return toString();
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
