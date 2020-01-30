package org.ikora.model;

import org.ikora.analytics.Action;
import org.ikora.analytics.visitor.NodeVisitor;
import org.ikora.analytics.visitor.VisitorMemory;
import org.ikora.utils.LevenshteinDistance;

import java.util.*;
import java.util.regex.Pattern;

public class ScalarVariable extends Variable {
    private Value value;

    public ScalarVariable(Token name){
        super(name);
        this.value = Value.empty();
    }

    public Value getValue(){
        return value;
    }

    @Override
    public void addElement(Token element){
        this.value = new Value(this, element);
    }

    @Override
    public List<Value> getValues() {
        return Collections.singletonList(this.value);
    }

    @Override
    public boolean isDeadCode(){
        return getDependencies().size() == 0;
    }

    @Override
    public double distance(Differentiable other) {
        if(!(other instanceof ScalarVariable)){
            return 1;
        }

        ScalarVariable variable = (ScalarVariable)other;
        double value = getName().equals(variable.getName()) ? 0 : 0.5;
        return value + (LevenshteinDistance.index(getValues(), variable.getValues()) / 2.0);
    }

    @Override
    public List<Action> differences(Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(!(other instanceof ScalarVariable)){
            actions.add(Action.invalid(this, other));
            return actions;
        }

        ScalarVariable variable = (ScalarVariable)other;

        if(LevenshteinDistance.index(getValues(), variable.getValues()) > 0){
            actions.add(Action.changeVariableDefinition(this, other));
        }

        return actions;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    protected void setName(Token name) {
        this.name = name;
        String generic = Value.getGenericVariableName(this.name.getText());
        String bareName = Value.escape(Value.getBareVariableName(generic));

        String patternString = String.format("^\\$\\{%s(((\\[\\d+\\])*)|([\\+\\-\\*/]\\d+))}$", bareName);
        this.pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
    }
}
