package com.github.jarnaud.fourten.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

@Slf4j
public class ExpressionTest {

    @Test
    public void test_isValid() {
        assertValid("4+2-3");
        assertValid("4*(2+1)-3");
        assertValid("(5+3)/4+8");
        assertInvalid("4+2-/3");
        assertInvalid("4+(2-3");
        assertInvalid("(5+3)/4+(8-2)");
        assertInvalid("1*4+)2-3");
        assertInvalid("1+2+3+");
        assertInvalid("+1+2+3");
        assertInvalid("-1+2+3"); // first negative not allowed in this game.
    }

    private void assertValid(String input) {
        Assertions.assertTrue(Expression.fromString(input).isValid());
    }

    private void assertInvalid(String input) {
        Assertions.assertFalse(Expression.fromString(input).isValid());
    }

    @Test
    public void test_evaluate() {
        assertEvaluation(10, "1+2+3+4");
        assertEvaluation(3.5, "(1+2*3)/2");
    }

    private void assertEvaluation(double val, String input) {
        Assertions.assertEquals(Optional.of(val), Expression.fromString(input).evaluate());
    }
}
