package com.github.jarnaud.fourten.validator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.jarnaud.fourten.validator.InputValidator.EXCEPTION_MESSAGE_INPUT_DIGITS;
import static com.github.jarnaud.fourten.validator.InputValidator.EXCEPTION_MESSAGE_INPUT_LENGTH;

@Slf4j
public class InputValidatorTest {

    @Test
    public void testInputLengthValidation() {
        assertInputLength(null, true);
        assertInputLength("", true);
        assertInputLength("0", true);
        assertInputLength("00", true);
        assertInputLength("000", true);
        assertInputLength("0000", false);
        assertInputLength("00000", true);
    }

    @Test
    public void testInputDigitsValidation() {
        assertInputDigits("000a", true);
        assertInputDigits("-+()", true);
        assertInputDigits(".!#@", true);
        assertInputDigits("0000", false);
        assertInputDigits("1234", false);
    }

    private void assertInputLength(String input, boolean errorExpected) {
        assertError(input, errorExpected, EXCEPTION_MESSAGE_INPUT_LENGTH);
    }

    private void assertInputDigits(String input, boolean errorExpected) {
        assertError(input, errorExpected, EXCEPTION_MESSAGE_INPUT_DIGITS);
    }

    private void assertError(String input, boolean errorExpected, String errorMessage) {
        if (errorExpected) {
            Exception e = Assertions.assertThrowsExactly(InputValidationException.class, () -> InputValidator.validate(input));
            Assertions.assertEquals(e.getMessage(), errorMessage);
        } else {
            Assertions.assertDoesNotThrow(() -> InputValidator.validate(input));
        }
    }
}
