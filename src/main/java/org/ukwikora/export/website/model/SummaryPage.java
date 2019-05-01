package org.ukwikora.export.website.model;

import org.ukwikora.analytics.Clone;
import org.ukwikora.analytics.Clones;
import org.ukwikora.model.Project;
import org.ukwikora.model.UserKeyword;
import org.ukwikora.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SummaryPage extends Page {
    private BarChart linesChart;
    private BarChart userKeywordsChart;
    private BarChart testCasesChart;
    private BarChart cloneChart;
    private List<String> scripts;

    private int linesOfCode;
    private int numberKeywords;
    private int numberTestCases;

    public SummaryPage(String id, String name, List<Project> projects, Clones<UserKeyword> clones) throws Exception {
        super(id, name);

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

            String projectName = project.getName();

            labels.add(StringUtils.toBeautifulName(projectName));
            lines.add(project.getLoc());
            userKeywords.add(project.getUserKeywords().size());
            testCases.add(project.getTestCases().size());
        }

        createLinesChart(labels, lines);
        createUserKeywordsChart(labels, userKeywords);
        createTestCasesChart(labels, testCases);
        createCloneChart(clones);

        setChartsHeight();

        scripts = new ArrayList<>(4);
        scripts.add(linesChart.getUrl());
        scripts.add(userKeywordsChart.getUrl());
        scripts.add(testCasesChart.getUrl());
        scripts.add(cloneChart.getUrl());
    }

    private void createTestCasesChart(List<String> labels, List<Integer> testCases) throws IOException {
        testCasesChart = new BarChart(
                "summary-test-cases-chart",
                "Test Cases",
                testCases,
                labels);

        testCasesChart.setYLabel("Number of Test Cases");
    }

    private void createUserKeywordsChart(List<String> labels, List<Integer> userKeywords) throws IOException {
        userKeywordsChart = new BarChart(
                "summary-user-keywords-chart",
                "User Keywords",
                userKeywords,
                labels);

        userKeywordsChart.setYLabel("Number User Keywords");
    }

    private void createLinesChart(List<String> labels, List<Integer> lines) throws IOException {
        linesChart = new BarChart(
                "summary-lines-of-code-chart",
                "Lines of Code",
                lines,
                labels);

        linesChart.setYLabel("Number Lines of Code");
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

    public BarChart getCloneChart() {
        return cloneChart;
    }

    public List<String> getScripts() {
        return scripts;
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


    private void setChartsHeight(){
        int height = 600;

        linesChart.setHeight(height);
        userKeywordsChart.setHeight(height);
        testCasesChart.setHeight(height);
    }

    private void createCloneChart(Clones<UserKeyword> clones) throws IOException {
        List<Double> values = new ArrayList<>();
        values.add(getPercentageClones(Clone.Type.TypeI, clones));
        values.add(getPercentageClones(Clone.Type.TypeII, clones));

        List<String> labels = new ArrayList<>();
        labels.add("Type I");
        labels.add("Type II");

        cloneChart = new BarChart(
                "summary-clones-chart",
                "Percentage of line duplicated",
                values,
                labels);
    }

    private double getPercentageClones(Clone.Type type, Clones<UserKeyword> clones){
        double numberOfCloneLines = 0;

        for(UserKeyword keyword: clones.getClones(type)){
            numberOfCloneLines += keyword.getLoc();
        }

        return (numberOfCloneLines / linesOfCode) * 100;
    }
}
