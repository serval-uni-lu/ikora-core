package tech.ikora.model;

import org.apache.commons.lang3.NotImplementedException;
import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.runner.Runtime;
import tech.ikora.utils.LevenshteinDistance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VariableAssignment extends SourceNode {
    private final Variable variable;
    private final NodeList<Argument> values;

    public VariableAssignment(Variable variable){
        this.variable = variable;
        this.values = new NodeList<>();
        this.addTokens(variable.getTokens());
    }

    public void addValue(SourceNode value) {
        this.addTokens(value.getTokens());
        this.values.add(new Argument(value));
    }

    public Variable getVariable(){
        return variable;
    }

    public NodeList<Argument> getValues() {
        return this.values;
    }

    @Override
    public boolean matches(Token name) {
        return variable.matches(name);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public void execute(Runtime runtime) throws Exception {
        throw new NotImplementedException("Runner is not implemented yet");
    }

    @Override
    public Token getNameToken() {
        return variable.getNameToken();
    }

    @Override
    public double distance(Differentiable other) {
        if(other == this){
            return 0;
        }

        if(!(other instanceof VariableAssignment)){
            return 1;
        }

        VariableAssignment assignment = (VariableAssignment)other;

        double distanceName = this.getNameToken().equalsIgnorePosition(assignment.getNameToken()) ? 0. : 0.5;
        double distanceValues = LevenshteinDistance.index(this.getValues(), assignment.getValues()) == 0. ? 0. : 0.5;

        return distanceName + distanceValues;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        if(other == this){
            return Collections.emptyList();
        }

        if(!(other instanceof VariableAssignment)){
            return Collections.singletonList(Action.changeType(this, other));
        }

        VariableAssignment assignment = (VariableAssignment)other;

        List<Action> actions = new ArrayList<>();

        if(!this.getName().equals(assignment.getName())){
            actions.add(Action.changeName(this, other));
        }

        actions.addAll(LevenshteinDistance.getDifferences(this.getValues(), assignment.getValues()));

        return actions;
    }
}
