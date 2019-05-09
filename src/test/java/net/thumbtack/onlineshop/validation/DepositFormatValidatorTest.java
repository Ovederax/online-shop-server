package net.thumbtack.onlineshop.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class DepositFormatValidatorTest {
    @Parameterized.Parameters(name = "{index}: validate: [{0}] need result {1}")
    public static Iterable<Object[]> names() {
        return Arrays.asList(new Object[][] {
                { "", false },
                { "-12", false },
                { "1+2", false },
                { "12", true },
                { "122211", true },
                { "0999", true }
        });
    }

    @Parameterized.Parameter(0)
    public String string;

    @Parameterized.Parameter(1)
    public boolean result;
    @Test
    public void isValidTest() {
        DepositFormatValidator validator = new DepositFormatValidator();
        assertEquals(result, validator.isValid(string, null));
    }
}