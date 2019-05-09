package net.thumbtack.onlineshop.model.exeptions;

import net.thumbtack.onlineshop.dto.response.ErrorContent;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ServerException extends Exception {
    private List<ErrorContent> errors;

    //primary constructor
    public ServerException(List<ErrorContent> errors) {
        this.errors = errors;
    }

    public ServerException(String error_code, String field, String message) {
        this(Collections.singletonList(new ErrorContent(error_code, field, message)));
    }

    public ServerException(ErrorCode exEnum) {
        this(exEnum.getErrorCode(), exEnum.getField(), exEnum.getMessage());
    }

    public static ServerException instanceFromErrorCodeList(List<ErrorCode> errors) {
        return new ServerException(errors.stream()
                .map(errorCode -> new ErrorContent(errorCode.getErrorCode(), errorCode.getField(), errorCode.getMessage()))
                .collect(Collectors.toList()));
    }

    public List<ErrorContent> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorContent> errors) {
        this.errors = errors;
    }
}
