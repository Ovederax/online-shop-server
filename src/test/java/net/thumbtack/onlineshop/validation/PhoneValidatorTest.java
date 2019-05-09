package net.thumbtack.onlineshop.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class PhoneValidatorTest {
    @Parameterized.Parameters(name = "{index}: validate: [{0}] need result {1}")
    public static Iterable<Object[]> names() {
        return Arrays.asList(new Object[][] {
                { "89136667890", true },
                { "8-913-666-78-90", true },
                { "+7-913-666-78-90", true },
                { "+7-913-666-78-90", true },
                { "8-9-1-3-6-6-6-7-8-9-0", true },
                { "6-9-1-3-6-6-6-7-8-9-0", false },
                { "0999", false },
                { "891366678910", false },
                { "89136t67891", false },
                { "89136--667890", false },
        });
    }

    @Parameterized.Parameter(0)
    public String string;

    @Parameterized.Parameter(1)
    public boolean result;
    @Test
    public void isValidTest() {
        PhoneValidator validator = new PhoneValidator();
        assertEquals(result, validator.isValid(string, null));
    }
}