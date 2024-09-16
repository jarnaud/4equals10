package com.github.jarnaud.fourten;

import com.github.jarnaud.fourten.model.Operator;
import com.github.jarnaud.fourten.solver.Solver;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class Main {

    public static void main(String[] args) {
        log.info("=== Starting program ===");

//        Set<String> results = Solver.solve("3478"); // hard

        Set<String> results = Solver.solve("2437", Set.of(Operator.ADD));
//        Set<String> results = Solver.solve("4428", Set.of(Operator.ADD));

        for (String res : results) {
            log.info("Result: {}", res);
        }
    }
}
