package net.thumbtack.onlineshop.model.exeptions.enums;

// REVU seems you have plans to add other enums
// what about next idea ?
// convert this enum to ErrorCode and add additional field "type"
// which will be "user" for these values and something else for other values
public enum UserExceptionEnum {
    //VALIDATE ERROR
    BAD_FIRST_NAME  ("Your firsname is not correct"),
    BAD_LAST_NAME   ("Your lastname is not correct"),
    BAD_LOGIN       ("Your login is bad"),
    BAD_PASSWORD    ("Your password is bad"),
    BAD_POSITION    ("Position is not correct"),
    //DATABASE ERROR
    BAD_LOGIN_DB("This login is used"),
    LOGIN_NOT_FOUND_DB("This login is not found"),
    PASSWORD_IS_NOT_CORRECT_DB("Your input password is invalid"),
    UUID_NOT_FOUND            ("Your uuid is not found"),
    USER_IS_INACTIVE("User is inactive now, no need logout account"),
    USER_IS_ACTIVE("User is active now, no need login account"),
    USER_IS_NOT_ADMIN("User is not administrator");


    private String description;

    UserExceptionEnum(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
