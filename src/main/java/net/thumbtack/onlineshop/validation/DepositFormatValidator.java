package net.thumbtack.onlineshop.validation;


import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class DepositFormatValidator implements ConstraintValidator<DepositFormat, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepositFormatValidator.class);

    @Override
    public boolean isValid(String money, ConstraintValidatorContext constraintValidatorContext) {
        LOGGER.debug("Validate money: \"" + money +"\"");
        if (money == null) {
            LOGGER.debug("Validate money is null");
            return true;                            // use @NotNull if need
        }
        try {
            return Integer.parseInt(money) > 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
