package com.github.jarnaud.fourten.validator;

public class InputValidator {

    static final String EXCEPTION_MESSAGE_INPUT_LENGTH = "Input must be 4 digits";
    static final String EXCEPTION_MESSAGE_INPUT_DIGITS = "Input must be digits only";

    public static void validate(String input) throws InputValidationException {
        validateLength(input);
        validateDigits(input);
    }

    private static void validateLength(String input) throws InputValidationException {
        if (input == null || input.length() != 4) {
            throw new InputValidationException(EXCEPTION_MESSAGE_INPUT_LENGTH);
        }
    }

    private static void validateDigits(String input) throws InputValidationException {
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c < '0' || c > '9') {
                throw new InputValidationException(EXCEPTION_MESSAGE_INPUT_DIGITS);
            }
        }
    }
}
