package org.ukwikora.export.website.model;

import org.ukwikora.model.Project;
import org.ukwikora.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SummaryPage {
    private BarChart linesChart;
    private BarChart userKeywordsChart;
    private BarChart testCasesChart;
    private List<String> scripts;

    private int linesOfCode;
    private int numberKeywords;
    private int numberTestCases;

    public SummaryPage(List<Project> projects) throws Exception {
        linesOfCode = 0;
        numberKeywords = 0;
        numberTestCases = 0;

        int size = projects.size();

        List<String> labels = new ArrayList<>(size);
        List<Integer> lines = new ArrayList<>(size);
        List<Integer> userKeywords = new ArrayList<>(size);
        List<Integer> testCases = new ArrayList<>(size);

        for(Project project: projects){
            linesOfCode += project.getLoc();
            numberKeywords += project.getUserKeywords().size();
            numberTestCases += project.getTestCases().size();

            String name = project.getName();

            labels.add(StringUtils.toBeautifulName(name));
            lines.add(project.getLoc());
            userKeywords.add(project.getUserKeywords().size());
            testCases.add(project.getTestCases().size());
        }

        linesChart = new BarChart(
                "summary-lines-of-code-chart",
                "Lines of Code",
                lines,
                labels);

        linesChart.setYLabel("Number Lines of Code");

        userKeywordsChart = new BarChart(
                "summary-user-keywords-chart",
                "User Keywords",
                userKeywords,
                labels);

        userKeywordsChart.setYLabel("Number User Keywords");

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

    public String getTitle(){
        return "Ukwikora - Dashboard";
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public int getNumberKeywords() {
        return numberKeywords;
    }

    public int getNumberTestCases() {
        return numberTestCases;
    }
}
