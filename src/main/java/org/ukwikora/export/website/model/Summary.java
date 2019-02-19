package org.ukwikora.export.website.model;

import org.ukwikora.export.website.model.BarChart;
import org.ukwikora.model.Project;
import org.ukwikora.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Summary {
    private BarChart linesChart;
    private BarChart userKeywordsChart;
    private BarChart testCasesChart;
    private List<String> scripts;

    public Summary(List<Project> projects) throws Exception {
        int size = projects.size();

        List<String> labels = new ArrayList<>(size);
        List<Integer> lines = new ArrayList<>(size);
        List<Integer> userKeywords = new ArrayList<>(size);
        List<Integer> testCases = new ArrayList<>(size);

        for(Project project: projects){
            String name = project.getName();

            labels.add(StringUtils.toBeautifulName(name));
            lines.add(project.getLoc());
            userKeywords.add(project.getUserKeywords().size());
            testCases.add(project.getTestCases().size());
        }

        linesChart = new BarChart(
                "summary-lines-of-code-chart",
                "User Keywords",
                lines,
                labels);

        linesChart.setYLabel("Number User Keywords");

        userKeywordsChart = new BarChart(
                "summary-user-keywords-chart",
                "User Keywords",
                userKeywords,
                labels);

        linesChart.setYLabel("Number Lines of Code");

        testCasesChart = new BarChart(
                "summary-test-cases-chart",
                "Test Cases",
                testCases,
                labels);

        testCasesChart.setYLabel("Number of Test Cases");

        scripts = new ArrayList<>(3);
        scripts.add(linesChart.getUrl());
        scripts.add(userKeywordsChart.getUrl());
        scripts.add(testCasesChart.getUrl());
    }

    public BarChart getLinesChart() {
        return linesChart;
    }

    public BarChart getUserKeywordsChart() {
        return userKeywordsChart;
    }

    public BarChart getTestCasesChart() {
        return testCasesChart;
    }

    public List<String> getScripts() {
        return scripts;
    }
}
