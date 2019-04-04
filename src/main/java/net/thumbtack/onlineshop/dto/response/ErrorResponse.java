package net.thumbtack.onlineshop.dto.response;

import java.util.List;
import java.util.Objects;

public class ErrorResponse {
    private List<ErrorContent> errors;

    public ErrorResponse(List<ErrorContent> errors) {
        this.errors = errors;
    }

    public ErrorResponse() {
    }

    public List<ErrorContent> getErrors() {
        return errors;
    }
    public void setErrors(List<ErrorContent> errors) {
        this.errors = errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponse that = (ErrorResponse) o;
        return Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {

        return Objects.hash(errors);
    }
}