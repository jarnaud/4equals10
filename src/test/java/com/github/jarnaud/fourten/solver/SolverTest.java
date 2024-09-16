package com.github.jarnaud.fourten.solver;

import com.github.jarnaud.fourten.model.Operator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

/**
 * Test for the Solver component.
 * Warning: contains spoilers for the game!
 */
@Slf4j
public class SolverTest {

    @Test
    public void testInputValidation() {
        Assertions.assertThrows(RuntimeException.class, () -> Solver.solve(null));
        Assertions.assertThrows(RuntimeException.class, () -> Solver.solve(""));
        Assertions.assertThrows(RuntimeException.class, () -> Solver.solve("123"));
        Assertions.assertThrows(RuntimeException.class, () -> Solver.solve("123*"));
        Assertions.assertDoesNotThrow(() -> Solver.solve("1234"));
    }

    @Test
    public void testNoSolutions() {
        test("1111", Set.of());
    }

    @Test
    public void testForbiddenOperators() {
        test("2437", Set.of(Operator.ADD), Set.of("(3*4-7)*2", "2-(3-7-4)", "2*(3*4-7)", "7-(3-2-4)", "2-(3-4-7)", "7-(3-4-2)", "4-(3-7-2)", "4-(3-2-7)", "(4*3-7)*2", "2*(4*3-7)"));
    }

    @Test
    public void testGameLevelSolutions() {
        test("3478", Set.of("8*(3-7/4)", "(3-7/4)*8"));
        test("5181", Set.of("8/(1-1/5)")); // level 500.
    }

    private void test(String input, Set<String> expected) {
        test(input, Collections.emptySet(), expected);
    }

    private void test(String input, Set<Operator> forbidden, Set<String> expected) {
        Set<String> solutions = Solver.solve(input, forbidden);
        Assertions.assertEquals(expected, solutions);
    }
}
