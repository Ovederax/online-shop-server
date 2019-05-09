package net.thumbtack.onlineshop.validation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NameValidator implements ConstraintValidator<NameFormat, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NameValidator.class);

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        LOGGER.debug("Validate name: \"" + name + "\"");
        if (name == null) {
            LOGGER.debug("Validate name is null");
            return true;                                // use @NotNull if need
        }
        Pattern pattern = Pattern.compile("[^а-яА-ЯёЁ]");
        Matcher matcher = pattern.matcher(name);
        return !matcher.find();       // find non assert symbols
    }
}
