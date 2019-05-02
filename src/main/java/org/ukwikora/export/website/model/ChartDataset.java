package org.ukwikora.export.website.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ukwikora.utils.JsonUtils;

import java.io.IOException;
import java.util.List;

public class ChartDataset {
    enum Color{
        RED,
        BLUE,
        GREEN
    }

    private final String label;
    private final Color color;
    private final String jsonValues;

    public ChartDataset(String label, List<? extends Number> values, Color color) throws IOException {
        this.label = label;
        this.color = color;
        this.jsonValues = JsonUtils.convertToJsonArray(values);
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    @JsonProperty("backgroundColor")
    public String getColor() {
        switch (color){
            case RED: return "window.chartColors.red";
            case BLUE: return "window.chartColors.blue";
            case GREEN: return "window.chartColors.green";
        }

        return "";
    }

    @JsonProperty("stack")
    public String getStack() {
        return "Stack 0";
    }

    @JsonProperty("data")
    public String getJsonValues() {
        return jsonValues;
    }
}
