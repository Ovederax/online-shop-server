package net.thumbtack.onlineshop.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.*;
@RunWith(Parameterized.class)
public class LoginValidatorTest {
    @Parameterized.Parameters(name = "{index}: validate: [{0}] need result {1}")
    public static Iterable<Object[]> names() {
        return Arrays.asList(new Object[][] {
                {"", true },
                { "Login", true },
                { "eeeChuvak", true },
                { "ss221", false },
                { "2dd21", false },
                { "Степан", true },
                { "VivВив", true },
                { "V13", false },
                { "выава", true },
                { "выава)", false },
                { "выава_", false }
        });
    }

    @Parameterized.Parameter(0)
    public String string;

    @Parameterized.Parameter(1)
    public boolean result;
    @Test
    public void isValidTest() {
        LoginValidator validator = new LoginValidator();
        assertEquals(result, validator.isValid(string, null));
    }
}