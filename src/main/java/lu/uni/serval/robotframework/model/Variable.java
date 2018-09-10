package lu.uni.serval.robotframework.model;

import lu.uni.serval.analytics.Action;
import lu.uni.serval.utils.Differentiable;

import java.util.ArrayList;
import java.util.List;

public class Variable implements Differentiable {
    private String  name;
    private List<Argument> definition;

    public Variable() {
        definition = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addValueElement(String element) {
        this.definition.add(new Argument(element));
    }

    public String getName() {
        return name;
    }

    public List<Argument> getValue() {
        return definition;
    }

    @Override
    public double distance(Differentiable other) {
        if(!(other instanceof Variable)){
            return 1;
        }

        Variable variable = (Variable)other;
        return name.equals(variable.name) ? 0 : 1;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        return null;
    }
}
