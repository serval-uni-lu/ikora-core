package lu.uni.serval.robotframework.report;

import java.util.ArrayList;
import java.util.List;

public class Report {
    private List<Result> results;

    public Report() {
        results = new ArrayList<Result>();
    }

    public void addResult(Result result) {
        results.add(result);
    }
}
