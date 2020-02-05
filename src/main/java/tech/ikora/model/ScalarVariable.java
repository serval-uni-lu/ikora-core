package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.ValueLinker;
import tech.ikora.utils.LevenshteinDistance;

import java.util.*;
import java.util.regex.Pattern;

public class ScalarVariable extends Variable {
    public ScalarVariable(Token name){
        super(name);
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
        boolean same = getName().equalsValue(variable.getName());
        same &= LevenshteinDistance.index(getArguments(), variable.getArguments()) == 0.0;

        return same ? 0.0 : 1.0;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(!(other instanceof ScalarVariable)){
            actions.add(Action.invalid(this, other));
            return actions;
        }

        ScalarVariable variable = (ScalarVariable)other;

        if(LevenshteinDistance.index(getArguments(), variable.getArguments()) > 0){
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
        String generic = ValueLinker.getGenericVariableName(this.name.getText());
        String bareName = ValueLinker.escape(ValueLinker.getBareVariableName(generic));

        String patternString = String.format("^\\$\\{%s(((\\[\\d+\\])*)|([\\+\\-\\*/]\\d+))}$", bareName);
        this.pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
    }
}
