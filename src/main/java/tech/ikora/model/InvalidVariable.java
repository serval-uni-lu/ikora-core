package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;

import java.util.ArrayList;
import java.util.List;

public class InvalidVariable extends Variable {
    public InvalidVariable() {
        super(Token.fromString("INVALID"));
    }

    @Override
    protected void setName(Token name) {
        this.name = name;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        //nothing to do
    }

    @Override
    public double distance(Differentiable other) {
        if(other instanceof InvalidVariable){
            return 0.0;
        }

        return 1.0;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(!(other instanceof InvalidStep)){
            actions.add(Action.changeVariableDefinition(this, other));
        }

        return actions;
    }
}
