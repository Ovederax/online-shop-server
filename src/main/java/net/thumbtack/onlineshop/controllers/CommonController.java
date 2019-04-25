package net.thumbtack.onlineshop.controllers;

import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;
import org.springframework.validation.BindingResult;

import javax.servlet.http.Cookie;

abstract class CommonController {
    void verifyValidateServerException(BindingResult result, Cookie cookie) throws ServerException {
        verifyCookie(cookie);
        verifyValidateServerException(result);
    }
    void verifyValidateServerException(BindingResult result) throws ServerException {
        if (!result.hasErrors()) {
            return;
        }
        ErrorCode errorCode = ErrorCode.VALIDATE_ERROR;
        errorCode.setField(result.getFieldError().getField());
        errorCode.setMessage(result.getFieldError().getDefaultMessage() + "RejectedValue: " + result.getFieldError().getRejectedValue());
        throw new ServerException(errorCode);
    }
    void verifyCookie(Cookie cookie) throws ServerException {
        if(cookie == null) {
            throw new ServerException(ErrorCode.YOU_ARE_NOT_LOGIN);
        }
    }
}
