package org.ukwikora.model;

import org.ukwikora.analytics.Action;
import org.ukwikora.analytics.VisitorMemory;
import org.ukwikora.utils.LevenshteinDistance;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.regex.Pattern;

public class ScalarVariable extends Variable {
    private Value value;

    public ScalarVariable(String name){
        super(name);
        this.value = Value.empty();
    }

    @Override
    public String getValueAsString() {
        return this.value.toString();
    }

    public Value getValue(){
        return value;
    }

    @Override
    public void addElement(String element){
        this.value = new Value(element);
    }

    @Override
    public List<Value> getValues() {
        return Collections.singletonList(this.value);
    }

    @Override
    public Optional<List<Value>> getResolvedValues() {
        if(this.isAssignment()){
            return Optional.empty();
        }

        return getValue().getResolvedValues();
    }

    @Override
    public double distance(@Nonnull Differentiable other) {
        if(!(other instanceof ScalarVariable)){
            return 1;
        }

        ScalarVariable variable = (ScalarVariable)other;
        double value = getName().equals(variable.getName()) ? 0 : 0.5;
        return value + (LevenshteinDistance.index(getValues(), variable.getValues()) / 2.0);
    }

    @Override
    public List<Action> differences(@Nonnull Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(!(other instanceof ScalarVariable)){
            return actions;
        }

        ScalarVariable variable = (ScalarVariable)other;

        if(LevenshteinDistance.index(getValues(), variable.getValues()) > 0){
            actions.add(Action.changeVariableDefinition(this, other));
        }

        return actions;
    }

    @Override
    public void accept(StatementVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    protected void setName(String name) {
        this.name = name;
        this.pattern = Pattern.compile(Value.escape(name), Pattern.CASE_INSENSITIVE);
    }
}
