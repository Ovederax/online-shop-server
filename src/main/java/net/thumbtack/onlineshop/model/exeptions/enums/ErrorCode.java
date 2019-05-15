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
    USER_IS_ACTIVE      ("token",   "User is active now, no need login account"),
    UUID_NOT_FOUND              ("token",    "Your uuid is not found"),
    EDIT_NOT_YOUR_TYPE_USER     ("token",    "You edit not your type of user"),
    YOU_NO_HAVE_THIS_PRIVILEGES ("token",    "You no have this privileges"),
    USER_IS_NOT_CLIENT("token", "User is not client" ),

    CATEGORY_NO_EXISTS("id",   "This category is no exist"),
    BAD_SET_SUBCATEGORY_INTO_SUBCATEGORY("parentId", "You no can set subcategory into subcategory"),
    UPDATE_PRODUCT_SET_CATEGORIES_NO_SUPPORT("categories", "You no can set categories in product where update it"),
    BAD_ORDER_FOR_GET_PRODUCT_LIST("order", "Bad order for get products list"),
    BAD_UPDATE_PRODUCT_IN_BASKET("", "Bad update product in basket"),
    NO_BUY_IF_PRODUCT_IS_CHANGE("field", "No make buy if product is changed"),
    YOU_NEED_MORE_MONEY_TO_BUY("money", "You need more money to buy product"),
    BUY_COUNT_PRODUCT_LESS_NEED_COUNT("count", "You no can buy product with this count"),
    NO_BUY_IF_CLIENT_DEPOSIT_IS_CHANGE("money", "You no can buy product if your money deposit is change"),
    NO_BUY_PRODUCT_IF_NAME_IS_CHANGE("name", "You cant buy product if name is change"),
    NO_BUY_PRODUCT_IF_PRICE_IS_CHANGE("price", "You cant buy product if price is change"),
    NO_BUY_PRODUCT_IF_COUNT_IS_CHANGE("count", "You cant buy product if count is change"),
    BAD_UPDATE_DEPOSIT_IT_IS_CHANGE("money", "Bad update deposit, deposit is change"),
    PRODUCT_IS_DELETED("product", "Product is deleted"),


    //-----DATABASE COMMON_ERROR---------------

    NOT_FOUND("","Page not found"),
    INTERNAL_SERVER_ERROR("", "Internal server error"),

    //--------MY_SQL_EXCEPTION------------------


    //-----------REQUEST ERROR-------------
    NULL_REQUEST("", "No data in Request"),
    COOKIE_MISSING("", "Cookie is missing"),

    //MySQLIntegrityConstraintViolationException
    CANT_ADD_PRODUCT_WITH_NO_UNIQUE_NAME("name", "Cant add product with no unique name"),
    CANT_ADD_CATEGORY_WITH_NO_UNIQUE_NAME("name", "Cant add category with no unique name"),

    // EXEPTION IN DaoImpl
    CANT_ADD_PRODUCT_TO_BASKET("", "Cant add product to basket"),
    CANT_GET_PRODUCT_FROM_BASKET("", "Cant get product from basket"),
    CANT_DELETE_FROM_BASKET("", "Cant delete from basket"),
    CANT_UPDATE_BASKET_ITEM("", "Cant update basket record"),
    CANT_ADD_CATEGORY("", "Cant add new category"),
    CANT_GET_CATEGORY("", "Cant get category"),
    CANT_UPDATE_CATEGORY("", "Cant update category"),
    CANT_DELETE_CATEGORY("", "Cant delete category"),
    CANT_CLEAR_DATABASE("", "Cant clear database"),
    CANT_ADD_PRODUCT("", "Cant add product"),
    CANT_GET_PRODUCT("", "Cant get product"),
    CANT_UPDATE_PRODUCT("", "Cant update product"),
    CANT_DELETE_PRODUCT("", "Cant delete product"),
    CANT_BUY_PRODUCT("", "Cant buy product"),
    CANT_BUY_PRODUCT_FROM_BASKET("", "Cant buy product from basket"),
    CANT_GET_PRODUCT_LIST("", "Cant get product list"),
    CANT_GET_CLIENTS("", "Cant get clients"),
    CANT_GET_CLIENT("", "Cant get client"),
    CANT_UPDATE_MONEY_DEPOSIT("", "Cant update money deposit"),
    CANT_USER_LOGOUT("", "Cant user logout"),
    CANT_GET_CLIENT_INFO("", "Cant get client info"),
    CANT_GET_ADMINISTRATOR("", "Cant get administrator"),
    CANT_UPDATE_ADMINISTRATOR("", "Cant update administrator"),
    CANT_UPDATE_CLIENT("", "Cant update client"),
    CANT_ADD_ADMINISTRATOR("", "Cant add administrator"),
    CANT_ADD_CLIENT("", "Cant add client"),
    CANT_GET_PURCHASES_LIST("", "Cant get purchases list");
    //------------------------------------------------------------------------------------------------

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
