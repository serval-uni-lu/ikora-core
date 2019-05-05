package org.ukwikora.export.website.model;

import org.ukwikora.utils.JsonUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class BarChart {
    private final String id;
    private final String name;
    private final String jsonDatasets;
    private final String jsonLabels;

    private String yLabel;
    private String xLabel;

    private double min;
    private double max;

    private int height;

    private boolean displayLegend;

    public BarChart(String id, String name, ChartDataset dataSet, List<String> labels) throws IOException {
        this.id = id;
        this.name = name;

        this.jsonDatasets = JsonUtils.convertToJsonArray(Collections.singletonList(dataSet));
        this.jsonLabels = JsonUtils.convertToJsonArray(labels);

        this.min = 0;
        this.max = dataSet.getMax();

        this.yLabel = "";
        this.xLabel = "";

        this.displayLegend = false;

        this.height = 300;
    }

    public BarChart(String id, String name, List<ChartDataset> dataSets, List<String> labels) throws IOException {
        this.id = id;
        this.name = name;
        this.jsonDatasets = JsonUtils.convertToJsonArray(dataSets);
        this.jsonLabels = JsonUtils.convertToJsonArray(labels);

        this.yLabel = "";
        this.xLabel = "";

        this.min = 0;
        this.max = dataSets.stream()
                .mapToDouble(ChartDataset::getMax)
                .max().orElse(0);

        this.displayLegend = dataSets.size() > 1;

        this.height = 300;
    }

    public double getMin(){
        return min;
    }

    public void setMin(double min){
        this.min = min;
    }

    public double getMax(){
        return max;
    }

    public void setMax(double max){
        this.max = max;
    }

    public boolean isDisplayLegend() {
        return displayLegend;
    }

    public void setDisplayLegend(boolean displayLegend) {
        this.displayLegend = displayLegend;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getJsonDatasets() {
        return jsonDatasets;
    }

    public String getJsonLabels() {
        return jsonLabels;
    }

    public String getYLabel() {
        return yLabel;
    }

    public void setYLabel(String yLabel) {
        this.yLabel = yLabel;
    }

    public String getXLabel() {
        return xLabel;
    }

    public void setXLabel(String xLabel) {
        this.xLabel = xLabel;
    }

    public String getUrl(){
        return String.format("js/%s.js", getId());
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
