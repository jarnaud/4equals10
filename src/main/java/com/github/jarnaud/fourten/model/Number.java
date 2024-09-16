package com.github.jarnaud.fourten.model;

/**
 * An operand.
 *
 * @param val the actual value.
 */
public record Number(int val) implements Element {

    public static Number of(int val) {
        return new Number(val);
    }

    @Override
    public String toString() {
        return "[" + val + "]";
    }
}
