package net.thumbtack.onlineshop.validation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class MinPasswordLengthValidator implements ConstraintValidator<MinPasswordLength, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinPasswordLengthValidator.class);

    @Value("${min_password_length}")
    private int minPasswordLength;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        LOGGER.debug("Validate value by min length: \"" + password +"\"");
        if (password == null) {
            LOGGER.debug("Validate password is null");
            return true;                        // use @NotNull if need
        }
        return password.length() >= minPasswordLength;
    }

}
