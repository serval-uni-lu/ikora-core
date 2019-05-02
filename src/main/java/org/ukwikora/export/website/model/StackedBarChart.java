package org.ukwikora.export.website.model;

import org.ukwikora.utils.JsonUtils;

import java.io.IOException;
import java.util.List;

public class StackedBarChart {
    private final String id;
    private final String name;
    private final String dataSets;

    private String yLabel;
    private String xLabel;

    private int height;

    public StackedBarChart(String id, String name, List<ChartDataset> dataSets) throws IOException {
        this.id = id;
        this.name = name;
        this.dataSets = JsonUtils.convertToJsonArray(dataSets);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDatasets(){
        return dataSets;
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
