package org.ukwikora.export.website.model;

import org.ukwikora.utils.JsonUtils;

import java.io.IOException;
import java.util.List;

public class BarChart {
    private final String id;
    private final String name;
    private final String jsonValues;
    private final String jsonLabels;

    private String yLabel;
    private String xLabel;

    public BarChart(String id, String name, List<Integer> values, List<String> labels) throws IOException {
        this.id = id;
        this.name = name;
        this.jsonValues = JsonUtils.convertToJsonArray(values);
        this.jsonLabels = JsonUtils.convertToJsonArray(labels);

        this.yLabel = "";
        this.xLabel = "";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getJsonValues() {
        return jsonValues;
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
}
