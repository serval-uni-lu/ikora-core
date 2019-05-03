package org.ukwikora.export.website.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
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

    @JsonRawValue
    private final String data;

    @JsonIgnore
    private final double max;

    public ChartDataset(String label, List<? extends Number> values, Color color) throws IOException {
        this.label = label;
        this.color = color;
        this.data = JsonUtils.convertToJsonArray(values);

        this.max = values.stream()
                .mapToDouble(Number::doubleValue)
                .max().orElse(0);
    }

    public double getMax() {
        return max;
    }

    public String getLabel() {
        return label;
    }

    public String getBackgroundColor() {
        switch (color){
            case RED: return "rgb(255, 99, 132)";
            case BLUE: return "rgb(54, 162, 235)";
            case GREEN: return "rgb(75, 192, 192)";
        }

        return "";
    }

    public String getData() {
        return data;
    }
}
