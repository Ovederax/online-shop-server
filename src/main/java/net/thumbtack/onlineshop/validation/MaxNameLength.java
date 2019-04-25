package net.thumbtack.onlineshop.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaxNameLengthValidator.class)
public @interface MaxNameLength {

    String message() default "Input field too long";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
