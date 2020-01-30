package org.ikora.model;

import org.ikora.analytics.Action;
import org.ikora.analytics.visitor.NodeVisitor;
import org.ikora.analytics.visitor.VisitorMemory;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.runner.Runtime;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Argument extends Node {
    private final Value value;
    private KeywordCall call;

    public Argument(Node parent, Token name) throws InvalidDependencyException {
        this.value = new Value(parent, name);
        this.addDependency(parent);
    }

    public Optional<KeywordCall> getCall() {
        return Optional.ofNullable(this.call);
    }

    public void setCall(KeywordCall call){
        this.call = call;
        this.call.setSourceFile(getSourceFile());
    }

    @Override
    public Value getNameAsValue() {
        return value;
    }

    @Override
    public boolean matches(Token name) {
        return value.matches(name);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public void execute(Runtime runtime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double distance(Differentiable other) {
        if(other == this){
            return 0.0;
        }

        if(other == null || !Argument.class.isAssignableFrom(other.getClass())){
            return 1.0;
        }

        return this.value.distance(((Argument)other).value);
    }

    @Override
    public List<Action> differences(Differentiable other) {
        if(other == this){
            return Collections.emptyList();
        }

        if(!Argument.class.isAssignableFrom(other.getClass())){
            return Collections.singletonList(Action.addElement(this.getClass(), this));
        }

        if(this.value.getToken().equalsValue(((Argument)other).value.getToken())){
            return Collections.emptyList();
        }

        return Collections.singletonList(Action.changeStepArgument(this, other));
    }

    @Override
    public Token getName() {
        return this.value.getToken();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
