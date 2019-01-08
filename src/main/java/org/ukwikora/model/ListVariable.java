package org.ukwikora.model;

import org.ukwikora.analytics.Action;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ListVariable extends Variable {
    private List<Value> values;

    public ListVariable(){
        this.values = new ArrayList<>();
    }

    @Override
    public String getValueAsString() {
        StringBuilder builder = new StringBuilder();

        for(Value value: values){
            builder.append(value.toString());
            builder.append("\t");
        }

        return builder.toString().trim();
    }

    @Override
    public void addElement(String element) {
        values.add(new Value(element));
    }

    @Override
    public List<Value> getValues() {
        return values;
    }

    @Override
    public double distance(@Nonnull Differentiable other) {
        return 0;
    }

    @Override
    public List<Action> differences(@Nonnull Differentiable other) {
        return null;
    }
}
