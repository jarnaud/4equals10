package com.github.jarnaud.fourten.solver;

import com.github.jarnaud.fourten.model.Expression;
import com.github.jarnaud.fourten.model.Number;
import com.github.jarnaud.fourten.model.Operator;
import com.github.jarnaud.fourten.model.Parentheses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A state in the exploration tree.
 */
class State {

    private final List<Number> remainingNumbers; // remaining numbers.
    private boolean remainingOpen; // open parenthesis is limited to one.
    private boolean remainingClose; // close parenthesis is limited to one.
    private final Expression expression; // current expression.

    public State(List<Number> remainingNumbers) {
        this.remainingNumbers = new ArrayList<>(remainingNumbers);
        this.remainingOpen = true;
        this.remainingClose = true;
        this.expression = new Expression();
    }

    public State(State state) {
        this.remainingNumbers = new ArrayList<>(state.remainingNumbers);
        this.remainingOpen = state.remainingOpen;
        this.remainingClose = state.remainingClose;
        this.expression = new Expression(state.expression);
    }

    private State copyCurrent() {
        return new State(this);
    }

    public boolean hasNoRemainingNumbers() {
        return remainingNumbers.isEmpty();
    }

    public Expression getExpression() {
        return expression;
    }

    /**
     * Process the next possible states from this state.
     *
     * @param forbiddenOperators the forbidden operators.
     * @return the next states.
     */
    public List<State> nextStates(Set<Operator> forbiddenOperators) {
        if (remainingNumbers.isEmpty() && (remainingOpen || !remainingClose)) return Collections.emptyList();

        List<State> nextStates = new ArrayList<>();
        // Add numbers.
        for (int i = 0; i < remainingNumbers.size(); i++) {
            State next = copyCurrent();
            Number removed = next.remainingNumbers.remove(i);
            next.expression.add(removed);
            if (next.expression.isPartiallyValid())
                nextStates.add(next);
        }

        // Add operators.
        for (Operator op : Operator.values()) {

            // Skip forbidden operators.
            if (forbiddenOperators.contains(op)) continue;

            State next = copyCurrent();
            next.expression.add(op);
            if (next.expression.isPartiallyValid())
                nextStates.add(next);
        }

        // Add open parenthesis.
        if (remainingOpen) {
            State next = copyCurrent();
            next.expression.add(Parentheses.OPEN);
            next.remainingOpen = false;
            if (next.expression.isPartiallyValid())
                nextStates.add(next);
        }

        // Add close parenthesis only if the previous element is a number.
        if (remainingClose && expression.size() > 0 && expression.get(expression.size() - 1) instanceof Number) {
            State next = copyCurrent();
            next.expression.add(Parentheses.CLOSE);
            next.remainingClose = false;
            if (next.expression.isPartiallyValid())
                nextStates.add(next);
        }

        return nextStates;
    }

}
