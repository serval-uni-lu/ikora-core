package lu.uni.serval.robotframework.report;

import lu.uni.serval.utils.ConsoleColors;

import java.util.ArrayList;
import java.util.List;

public class Report {
    private List<Result> results;
    private String name;

    public Report() {
        results = new ArrayList<Result>();
    }

    public void addResult(Result result) {
        results.add(result);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void print() {
        System.out.println(name);

        for(Result result : results) {
            String color = result.isError() ? ConsoleColors.RED : ConsoleColors.GREEN;
            String message = color + "\t" + result.getMessage() + ConsoleColors.RESET;

            System.out.println(message);
        }
    }
}
