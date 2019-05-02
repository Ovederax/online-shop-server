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
        //example
        // 89136667890
        // 8-913-666-78-90
        // +7-913-666-78-90
        // 8-9-1-3-6-6-6-7-8-9-0
        return phone.matches("(8|\\+7)(-?\\d){10}");
    }
}
