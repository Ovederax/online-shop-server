package net.thumbtack.onlineshop.validation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginValidator implements ConstraintValidator<LoginFormat, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginValidator.class);

    @Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {
        LOGGER.debug("Validate login: \"" + login + "\"");
        if (login == null) {
            LOGGER.debug("Validate login is null");
            return true;                                       // use @NotNull if need
        }
        Pattern pattern = Pattern.compile("[^а-яА-ЯёЁa-zA-Z]");
        Matcher matcher = pattern.matcher(login);
        return !matcher.find();       // find non assert symbols
    }
}
