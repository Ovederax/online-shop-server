package net.thumbtack.onlineshop.validation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class MaxNameLengthValidator implements ConstraintValidator<MaxNameLength, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MaxNameLengthValidator.class);

    @Value("${max_name_length}")
    private int maxNameLength;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        LOGGER.debug("Validate value by max length: \"" + name+"\"");
        if (name == null) {
            LOGGER.debug("Validate name is null");
            return true;                            // use @NotNull if need
        }
        return name.length() <= maxNameLength;
    }
}
