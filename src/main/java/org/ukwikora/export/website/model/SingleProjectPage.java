package org.ukwikora.export.website.model;

import org.ukwikora.analytics.ProjectStatistics;
import org.ukwikora.model.Project;
import org.ukwikora.model.TestCase;
import org.ukwikora.model.UserKeyword;
import org.ukwikora.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SingleProjectPage extends Page {
    private final Link link;
    private final List<String> scripts;

    private final int numberKeywords;
    private final int numberTestCases;
    private final int linesOfCode;

    private final BarChart sizeChart;
    private final BarChart connectivityChart;
    private final BarChart depthChart;
    private final BarChart sequenceChart;

    public SingleProjectPage(Project project) throws Exception {
        super(
            StringUtils.toBeautifulUrl(project.getName(), ""),
            StringUtils.toBeautifulName(project.getName())
         );

        this.link = new Link(project.getName());

        ProjectStatistics statistics = new ProjectStatistics(project);

        this.numberKeywords = statistics.getNumberKeywords(UserKeyword.class);
        this.numberTestCases = statistics.getNumberKeywords(TestCase.class);
        this.linesOfCode = statistics.getLoc();

        this.sizeChart = createSizeChart(statistics);
        this.connectivityChart = createConnectivityChart(statistics);
        this.depthChart = createDepthChart(statistics);
        this.sequenceChart = createSequenceChart(statistics);

        this.scripts = new ArrayList<>(4);
        this.scripts.add(this.sizeChart.getUrl());
        this.scripts.add(this.connectivityChart.getUrl());
        this.scripts.add(this.depthChart.getUrl());
        this.scripts.add(this.sequenceChart.getUrl());
    }

    private BarChart createConnectivityChart(ProjectStatistics statistics) throws IOException {
        Map<Integer, Integer> connectivity = statistics.getConnectivityDistribution(UserKeyword.class);

        ChartDataset dataset = new ChartDataset("Number of Keywords", getValues(connectivity), ChartDataset.Color.BLUE);

        BarChart chart = new BarChart(
                String.format("%s-connectivity-chart", getId()),
                "Keywords Connectivity",
                dataset,
                getKeys(connectivity));

        chart.setYLabel("Number of Keywords");
        chart.setXLabel("Number of keywords depending on the keyword");

        return chart;
    }

    private BarChart createDepthChart(ProjectStatistics statistics) throws IOException {
        Map<Integer, Integer> depth = statistics.getLevelDistribution(UserKeyword.class);

        ChartDataset dataset = new ChartDataset("Number of Keywords", getValues(depth), ChartDataset.Color.BLUE);

        BarChart chart = new BarChart(
                String.format("%s-depth-chart", getId()),
                "Keywords Depth",
                dataset,
                getKeys(depth));

        chart.setYLabel("Number of Keywords");
        chart.setXLabel("Longest distance to a library keyword");

        return chart;
    }

    private BarChart createSequenceChart(ProjectStatistics statistics) throws IOException {
        Map<Integer, Integer> sequence = statistics.getSequenceDistribution(TestCase.class);

        ChartDataset dataset = new ChartDataset("Number of Test Cases", getValues(sequence), ChartDataset.Color.BLUE);

        BarChart chart = new BarChart(
                String.format("%s-sequence-chart", getId()),
                "Test Cases Sequence",
                dataset,
                getKeys(sequence));

        chart.setYLabel("Number of Test Cases");
        chart.setXLabel("Number of actions performed");

        return chart;
    }

    private BarChart createSizeChart(ProjectStatistics statistics) throws IOException {
        Map<Integer, Integer> size = statistics.getSizeDistribution(UserKeyword.class);

        ChartDataset dataset = new ChartDataset("Number of Keywords", getValues(size), ChartDataset.Color.BLUE);

        BarChart chart = new BarChart(
                String.format("%s-size-chart", getId()),
                "Keywords Size",
                dataset,
                getKeys(size));

        chart.setYLabel("Number of Keywords");
        chart.setXLabel("Number of keywords composing the keyword");

        return chart;
    }

    public Link getLink() {
        return link;
    }

    public int getNumberKeywords() {
        return numberKeywords;
    }

    public int getNumberTestCases() {
        return numberTestCases;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public BarChart getSizeChart() {
        return sizeChart;
    }

    public BarChart getConnectivityChart() {
        return connectivityChart;
    }

    public BarChart getDepthChart() {
        return depthChart;
    }

    public BarChart getSequenceChart() {
        return sequenceChart;
    }

    public List<String> getScripts() {
        return scripts;
    }

    private List<String> getKeys(Map<Integer, Integer> map){
        List<String> keys = new ArrayList<>(map.size());
        for (Map.Entry<Integer,Integer> entry : map.entrySet()){
            keys.add(String.valueOf(entry.getKey()));
        }
        return keys;
    }

    private List<Integer> getValues(Map<Integer, Integer> map){
        List<Integer> keys = new ArrayList<>(map.size());
        for (Map.Entry<Integer,Integer> entry : map.entrySet()){
            keys.add(entry.getValue());
        }
        return keys;
    }
}
