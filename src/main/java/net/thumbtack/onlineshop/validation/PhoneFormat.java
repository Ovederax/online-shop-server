package net.thumbtack.onlineshop.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
public @interface PhoneFormat {

    String message() default "Phone have error format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
