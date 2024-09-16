package com.github.jarnaud.fourten.model;


import lombok.extern.slf4j.Slf4j;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class Expression {

    private final List<Element> elements;

    public Expression() {
        this(new ArrayList<>());
    }

    public Expression(Expression expression) {
        this(expression.elements);
    }

    public Expression(List<Element> elements) {
        this.elements = new ArrayList<>(elements);
    }

    public void add(Element element) {
        this.elements.add(element);
    }

    public int size() {
        return this.elements.size();
    }

    public Element get(int i) {
        return this.elements.get(i);
    }

    public static Expression fromString(String input) {
        List<Element> elements = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            switch (c) {
                case '+' -> elements.add(Operator.ADD);
                case '-' -> elements.add(Operator.SUB);
                case '*' -> elements.add(Operator.MUL);
                case '/' -> elements.add(Operator.DIV);
                case '(' -> elements.add(Parentheses.OPEN);
                case ')' -> elements.add(Parentheses.CLOSE);
                default -> elements.add(Number.of(Integer.parseInt(input.substring(i, i + 1))));
            }
        }
        return new Expression(elements);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Element element : elements) {
            switch (element) {
                case Operator.ADD -> sb.append('+');
                case Operator.SUB -> sb.append('-');
                case Operator.MUL -> sb.append('*');
                case Operator.DIV -> sb.append('/');
                case Parentheses.OPEN -> sb.append('(');
                case Parentheses.CLOSE -> sb.append(')');
                default -> sb.append(((Number) element).val());
            }
        }
        return sb.toString();
    }

    public Optional<Double> evaluate() {
        log.debug("Evaluating {}", elements);
        if (!isValid()) {
            log.debug("Invalid expression");
            throw new RuntimeException("Cannot evaluate an invalid expression.");
        }

        // Use expression evaluator.
        net.objecthunter.exp4j.Expression exp = new ExpressionBuilder(this.toString()).build();
        try {
            return Optional.of(exp.evaluate());
        } catch (ArithmeticException e) {
            log.debug("Arithmetic exception", e);
            return Optional.empty();
        }
    }


    /**
     * Return true if this expression is at least partially valid, meaning that even if the expression is incomplete
     * its final state is potentially valid. This method is used to trim the exploration tree.
     *
     * @return true if partially valid, false otherwise.
     */
    public boolean isPartiallyValid() {
        boolean expectNumber = true; // true if expecting number, false if expecting operator.

        for (Element element : elements) {
            if (element instanceof Parentheses) continue; // skip checking of parenthesis for now.
            if (expectNumber && element instanceof Number) {
                expectNumber = false;
            } else if (!expectNumber && element instanceof Operator) {
                expectNumber = true;
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * Return true if this expression is totally valid (i.e. including parenthesis).
     *
     * @return true if valid, false otherwise.
     */
    public boolean isValid() {
        log.debug("Checking validity of {}", elements);

        boolean parenthesisUsed = false; // Limit usage to one pair of parenthesis.
        boolean expectOpen = true;
        boolean expectClose = false;
        boolean expectNumber = true;
        boolean expectOperator = false;

        for (Element element : elements) {

            if (expectOpen && element instanceof Parentheses par) {
                if (par == Parentheses.OPEN) {
                    parenthesisUsed = true;
                    expectOpen = false;
                    expectClose = true;
                } else {
                    log.debug("Invalid expression: open parenthesis allowed, but closed found.");
                    return false;
                }

            } else if (expectClose && element instanceof Parentheses par) {
                if (par == Parentheses.CLOSE) {
                    expectClose = false;
                } else {
                    log.debug("Invalid expression: close parenthesis allowed, but open found.");
                    return false;
                }

            } else if (expectNumber && element instanceof Number) {
                expectNumber = false;
                expectOpen = false;
                expectOperator = true;

            } else if (expectOperator && element instanceof Operator) {
                expectNumber = true;
                expectOperator = false;
                if (!parenthesisUsed) {
                    expectOpen = true;
                }

            } else {
                log.debug("Invalid expression: wrong element found: {}", element);
                return false;
            }
        }

        if (expectClose) {
            log.debug("Invalid expression: parenthesis not closed.");
            return false;
        }

        if (expectNumber) {
            log.debug("Invalid expression: finishing with an operator.");
            return false;
        }

        return true;
    }

}
