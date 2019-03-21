package net.thumbtack.onlineshop.dto.response;

import java.util.List;

public class ErrorResponse {
    private List<ErrorContent> errors;

    public ErrorResponse(List<ErrorContent> errors) {
        this.errors = errors;
    }

    public List<ErrorContent> getErrors() {
        return errors;
    }
    public void setErrors(List<ErrorContent> errors) {
        this.errors = errors;
    }
}