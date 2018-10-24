package org.ukwikora.model;

import org.ukwikora.analytics.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DictionaryVariable extends Variable {
    List<Value> values;

    public DictionaryVariable(){
        this.values = new ArrayList<>();
    }

    @Override
    public double distance(Differentiable other) {
        return 0;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        return null;
    }

    @Override
    public void addElement(String element) {
        values.add(new Value(element));
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
    public List<Value> getValues() {
        return values;
    }
}
