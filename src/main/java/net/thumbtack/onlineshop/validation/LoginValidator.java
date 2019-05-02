package net.thumbtack.onlineshop.validation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class LoginValidator implements ConstraintValidator<LoginFormat, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginValidator.class);

    @Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {
        LOGGER.debug("Validate login: \"" + login + "\"");
        if (login == null) {
            LOGGER.debug("Validate login is null");
            return true;                                       // use @NotNull if need
        }
        return !login.matches("[^а-яА-ЯёЁa-zA-Z]");       // find non assert symbols
    }
}
