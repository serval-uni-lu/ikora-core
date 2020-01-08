package org.ikora.model;

import org.ikora.analytics.Action;
import org.ikora.analytics.visitor.NodeVisitor;
import org.ikora.analytics.visitor.VisitorMemory;
import org.ikora.exception.InvalidTypeException;
import org.ikora.runner.Runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class InvalidStep extends Step {
    public InvalidStep(String name) {
        super(name);
    }

    @Override
    public Optional<KeywordCall> getKeywordCall() {
        return Optional.empty();
    }

    @Override
    public List<Argument> getArgumentList() {
        return Collections.emptyList();
    }

    @Override
    public boolean hasParameters() {
        return false;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        // should not be visited
    }

    @Override
    public void execute(Runtime runtime) throws Exception {
        throw new InvalidTypeException("Invalid step cannot be executed");
    }

    @Override
    public double distance(Differentiable other) {
        return other instanceof InvalidStep ? 0.0 : 1.0;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(!(other instanceof InvalidStep)){
            actions.add(Action.changeStepType(this, other));
        }

        return actions;
    }
}