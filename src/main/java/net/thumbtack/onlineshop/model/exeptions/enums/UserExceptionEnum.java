package net.thumbtack.onlineshop.model.exeptions.enums;

// REVU seems you have plans to add other enums
// what about next idea ?
// convert this enum to ErrorCode and add additional field "type"
// which will be "user" for these values and something else for other values
public enum UserExceptionEnum {
    //VALIDATE ERROR
    BAD_FIRST_NAME  ("firstname",   "Your firsname is not correct"),
    BAD_LAST_NAME   ("lastname",    "Your lastname is not correct"),
    BAD_LOGIN       ("login",       "Your login is bad"),
    BAD_PASSWORD    ("password",    "Your password is bad"),
    BAD_POSITION    ("position",    "Position is not correct"),
    //DATABASE ERROR
    LOGIN_ALREADY_EXISTS("login",   "This login is used"),
    LOGIN_NOT_FOUND_DB  ("login",   "This login is not found"),
    USER_IS_INACTIVE    ("token",   "User is inactive now, no need logout account"),
    USER_IS_ACTIVE      ("token",   "User is active now, no need login account"),
    USER_IS_NOT_ADMIN   ("token",   "User is not administrator"),
    PASSWORD_IS_NOT_CORRECT_DB  ("password", "Your input password is invalid"),
    UUID_NOT_FOUND              ("token",    "Your uuid is not found"),
    EDIT_NOT_YOUR_TYPE_USER     ("token",    "You edit not your type of user"),
    YOU_NO_HAVE_THIS_PRIVILEGES ("token",    "You no have this privileges"),
    USER_IS_NOT_CLIENT("token", "User is not client" );


    private String message;
    private String field;

    UserExceptionEnum(String field, String message) {
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
