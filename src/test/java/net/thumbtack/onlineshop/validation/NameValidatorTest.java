package net.thumbtack.onlineshop.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class NameValidatorTest {
    @Parameterized.Parameters(name = "{index}: validate: [{0}] need result {1}")
    public static Iterable<Object[]> names() {
        return Arrays.asList(new Object[][] {
                {"", true },
                { "Степан", true },
                { "выава", true },
                { "Login", false },
                { "eeeChuvak", false },
                { "ss221", false },
                { "2dd21", false },
                { "VivВив", false },
                { "V13", false },
                { "выаss", false },
                { "выава)", false },
                { "Сте2пан", false },
                { "выава_", false }
        });
    }

    @Parameterized.Parameter(0)
    public String string;

    @Parameterized.Parameter(1)
    public boolean result;
    @Test
    public void isValidTest() {
        NameValidator validator = new NameValidator();
        assertEquals(result, validator.isValid(string, null));
    }
}