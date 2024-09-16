package com.github.jarnaud.fourten.solver;

import com.github.jarnaud.fourten.model.Number;
import com.github.jarnaud.fourten.model.Operator;
import com.github.jarnaud.fourten.validator.InputValidationException;
import com.github.jarnaud.fourten.validator.InputValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Solver {

    /**
     * The target number, giving its name to the game.
     */
    private static final double TARGET = 10;

    /**
     * Find the expression for the given input.
     * Allows usage of all operators.
     *
     * @param input the four digits input.
     * @return the set of valid expression.
     */
    public static Set<String> solve(String input) {
        return solve(input, Collections.emptySet());
    }

    /**
     * Find the expression for the given input.
     * Forbid usage of specified operators.
     *
     * @param input              the four digits input.
     * @param forbiddenOperators an optional list of forbidden operators.
     * @return the set of valid expressions.
     */
    public static Set<String> solve(String input, Set<Operator> forbiddenOperators) {
        try {
            InputValidator.validate(input);
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }

        List<Number> numbers = extractNumbersFromInput(input);
        return solve(numbers, forbiddenOperators);
    }

    private static List<Number> extractNumbersFromInput(String input) {
        List<Number> numbers = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            numbers.add(Number.of(Integer.parseInt(input.substring(i, i + 1))));
        }
        return numbers;
    }

    private static Set<String> solve(List<Number> numbers, Set<Operator> forbiddenOperators) {
        log.debug("Solving for numbers: {}", numbers);

        // All solution states.
        List<State> solutions = new ArrayList<>();

        State initialState = new State(numbers);
        List<State> nextStates = initialState.nextStates(forbiddenOperators);
        while (!nextStates.isEmpty()) {
            State state = nextStates.removeFirst();

            // Evaluate potential solutions: states with all numbers used and a valid expression.
            if (state.hasNoRemainingNumbers() && state.getExpression().isValid()) {
                Optional<Double> val = state.getExpression().evaluate();
                if (val.isPresent() && val.get() == TARGET) {
                    solutions.add(state);
                }
            }

            // Add next states (if any).
            nextStates.addAll(state.nextStates(forbiddenOperators));
        }

        // Build set of return string.
        Set<String> results = new HashSet<>();
        if (solutions.isEmpty()) {
            log.info("No solution to this input.");
        } else {
            for (State solution : solutions) {
                results.add(solution.getExpression().toString());
            }
            log.info("Found {} solutions.", results.size());
        }
        return results;
    }

}
