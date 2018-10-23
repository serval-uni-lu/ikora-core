package org.ukwikora.report;

import org.ukwikora.utils.ConsoleColors;

import java.util.ArrayList;
import java.util.List;

public class ExecutionReport {
    private List<ExecutionResult> results;
    private String name;

    public ExecutionReport() {
        results = new ArrayList<>();
    }

    public void addResult(ExecutionResult result) {
        results.add(result);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void print() {
        System.out.println(name);

        for(ExecutionResult result : results) {
            String color = result.isError() ? ConsoleColors.RED : ConsoleColors.GREEN;
            String message = color + "\t" + result.getMessage() + ConsoleColors.RESET;

            System.out.println(message);
        }
    }
}
