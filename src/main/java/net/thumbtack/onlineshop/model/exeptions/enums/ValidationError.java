package net.thumbtack.onlineshop.model.exeptions.enums;

public final class ValidationError {
    //---------------PRODUCTS-----------------------
    public static final String ID_MUST_GREAT_ZERO               = "Id must have value great zero";
    public static final String PRODUCT_NAME_CANNOT_BE_NULL      = "Product name cannot be NULL";
    public static final String PRODUCT_PRICE_MUST_GREAT_ZERO    = "Price must great zero";
    public static final String PRODUCT_COUNT_CANNOT_BE_LESS_ZERO = "Product count cannot less zero";

    //---------------CATEGORY-----------------------
    public static final String CATEGORY_NAME_CANNOT_BE_NULL     = "Category name cannot be NULL";
    public static final String PARENT_ID_MUST_GREAT_ZERO        = "Category parent must have id greet zero";

    //---------------USER-----------------------
    public static final String FIRST_NAME_CANNOT_BE_NULL = "First name cannot be null";
    public static final String LAST_NAME_CANNOT_BE_NULL  = "Last name cannot be null";
    public static final String ADMINISTRATOR_POSITION_CANNOT_BE_NULL = "Administrator position cannot be null";
    public static final String ADDRESS_CANNOT_BE_NULL   = "Address cannot be null";
    public static final String ADDRESS_CANNOT_BE_EMPTY  = "Address cannot be empty";
    public static final String PHONE_HAVE_NO_CORRECT_FORMAT = "Phone have not correct format";
    public static final String DEPOSIT_CANNOT_BE_NULL = "Deposit cannot be null";

    public static final String LOGIN_CANNOT_BE_NULL = "Login cannot be null";
    public static final String PASSWORD_CANNOT_BE_NULL = "Password cannot be null";



    private ValidationError(){};
}
