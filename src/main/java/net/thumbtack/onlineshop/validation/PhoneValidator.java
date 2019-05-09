package net.thumbtack.onlineshop.validation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
    Допустимые телефонные номера - сотовые номера
    любых операторов России.
    Номер может начинаться как с “8”, так и с “+7”.
    Наличие в номере знаков “-” (дефис) ошибкой не является,
    но перед записью в БД все знаки “-” удаляются.
*/
public class PhoneValidator implements ConstraintValidator<PhoneFormat, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneValidator.class);

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        LOGGER.debug("Validate phone: \"" + phone + "\"");
        if (phone == null) {
            LOGGER.debug("Validate phone is null");
            return true;                            // use @NotNull if need
        }
        return phone.matches("(8|\\+7)(-?\\d){10}");
    }
}
