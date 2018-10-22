package lu.uni.serval.robotframework.model;

import lu.uni.serval.analytics.Action;
import lu.uni.serval.utils.Differentiable;
import lu.uni.serval.utils.LevenshteinDistance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScalarVariable extends Variable {
    private List<Value> definition;

    public ScalarVariable(){
        this.definition = new ArrayList<>();
    }

    @Override
    public void addElement(String element) {
        this.definition.add(new Value(element));
    }

    public List<Value> getValue() {
        return definition;
    }

    public String getValueAsString(){
        StringBuilder builder = new StringBuilder();

        for(Iterator<Value> i = definition.iterator(); i.hasNext();) {
            builder.append(i.next().toString());

            if(i.hasNext()){
                builder.append("\t");
            }
        }

        return builder.toString();
    }

    @Override
    public double distance(Differentiable other) {
        if(!(other instanceof ScalarVariable)){
            return 1;
        }

        ScalarVariable variable = (ScalarVariable)other;
        double value = getName().equals(variable.getName()) ? 0 : 0.5;
        return value + (LevenshteinDistance.index(definition, variable.definition) / 2.0);
    }

    @Override
    public List<Action> differences(Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(!(other instanceof ScalarVariable)){
            return actions;
        }

        ScalarVariable variable = (ScalarVariable)other;

        if(LevenshteinDistance.index(definition, variable.definition) > 0){
            actions.add(Action.changeVariableDefinition(this, other));
        }

        return actions;
    }
}
