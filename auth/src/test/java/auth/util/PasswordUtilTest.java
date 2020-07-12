package auth.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PasswordUtilTest {

    @Test
    void generatePassword() {
        final String actualPassword = PasswordUtil.generatePassword();
        assertNotNull(actualPassword);
        int digitCounter = 0;
        int upperCounter = 0;
        int lowerCounter = 0;
        for (char c : actualPassword.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCounter++;
            } else if (Character.isUpperCase(c)) {
                upperCounter++;
            } else if (Character.isLowerCase(c)) {
                lowerCounter++;
            }
        }
        assertEquals(4, digitCounter);
        assertEquals(4, upperCounter);
        assertEquals(4, lowerCounter);
    }
}