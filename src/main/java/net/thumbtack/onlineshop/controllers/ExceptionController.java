package net.thumbtack.onlineshop.controllers;

import net.thumbtack.onlineshop.dto.response.ErrorContent;
import net.thumbtack.onlineshop.dto.response.ErrorResponse;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;
import org.hibernate.validator.cfg.ConstraintDef;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@ControllerAdvice
public class ExceptionController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestCookieException.class)
    @ResponseBody
    ErrorResponse handleMissingCookieError(HttpServletRequest req, MissingRequestCookieException ex) {
        final ErrorCode code = ErrorCode.COOKIE_MISSING;
        return new ErrorResponse(singletonList(new ErrorContent(code.getErrorCode(), ex.getCookieName(), code.getMessage())));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    ErrorResponse handleValidationError(HttpServletRequest req, MethodArgumentNotValidException ex) {
        ArrayList<ErrorContent> list = new ArrayList<>(ex.getBindingResult().getFieldErrors().size());
        final String errorCode = ErrorCode.VALIDATE_ERROR.getErrorCode();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            list.add( new ErrorContent(errorCode, fieldError.getField(), fieldError.getDefaultMessage()) );
        }
        return new ErrorResponse(list);
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    ErrorResponse handleValidationError(HttpServletRequest req, ConstraintViolationException ex) {
        ArrayList<ErrorContent> list = new ArrayList<>();
        final String errorCode = ErrorCode.VALIDATE_ERROR.getErrorCode();
        for (ConstraintViolation it : ex.getConstraintViolations()) {
            list.add( new ErrorContent(errorCode, "", it.getMessage()) );
        }

        return new ErrorResponse(list);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServerException.class)
    @ResponseBody
    ErrorResponse handleServerException(HttpServletRequest req, ServerException ex) {
        return new ErrorResponse(asList(
                new ErrorContent( ex.getErrorCode(), ex.getField(), ex.getMessage() )
        ));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    ErrorResponse handleBadRequest(HttpServletRequest req, HttpMessageNotReadableException ex) {
        ErrorCode code = ErrorCode.NULL_REQUEST;
        return new ErrorResponse(asList(
            new ErrorContent(code.getErrorCode(), code.getField(), code.getMessage() )
        ));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingServletRequestParameterException.class, MissingServletRequestPartException.class})
    @ResponseBody
    ErrorResponse handleBadBody(HttpServletRequest req, ServletException ex) {
        // TODO Похоже это исключение для отсутствующих параметров
        // но в случае отсутствия json, выбрасывается HttpMessageNotReadableException, хмм
        return new ErrorResponse(asList( new ErrorContent(ErrorCode.NULL_REQUEST.getErrorCode(), "", ex.getMessage() )));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    ErrorResponse handleNoFoundException(HttpServletRequest req, NoHandlerFoundException ex) {
        return new ErrorResponse(asList(
            new ErrorContent(ErrorCode.NOT_FOUND.getErrorCode(), "", ErrorCode.NOT_FOUND.getMessage() )
        ));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    ErrorResponse handleInternalError(HttpServletRequest req, Throwable ex) {
        return new ErrorResponse(asList(
            new ErrorContent(ErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(), "", ErrorCode.INTERNAL_SERVER_ERROR.getMessage() )
        ));
    }
}

