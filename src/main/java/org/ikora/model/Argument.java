package org.ikora.model;

import org.ikora.analytics.Action;
import org.ikora.analytics.visitor.NodeVisitor;
import org.ikora.analytics.visitor.VisitorMemory;
import org.ikora.builder.ValueLinker;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.runner.Runtime;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Argument extends Node {
    public enum Type{
        STRING, OBJECT, KEYWORD, LOCATOR, CONDITION, KEYWORDS, KWARGS
    }

    private KeywordCall call;
    private final Token name;

    public Argument(Node parent, Token name) throws InvalidDependencyException {
        this.name = name;
        this.call = null;

        this.addDependency(parent);
    }

    public Argument(Node parent, KeywordCall call) throws InvalidDependencyException {
        this.addDependency(parent);

        if(call == null){
            this.name = Token.empty();
            this.call = null;
            return;
        }

        this.name = call.getName();
        this.call = call;
        call.addDependency(this);
    }

    public Optional<KeywordCall> getCall() {
        return Optional.ofNullable(this.call);
    }

    public void setCall(KeywordCall call){
        this.call = call;
        this.call.setSourceFile(getSourceFile());
    }

    @Override
    public boolean matches(Token name) {
        return ValueLinker.matches(this.name, name);
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

        Argument argument = (Argument)other;

        boolean sameCall = true;
        if(this.call != null && this.call != argument.call){
            sameCall = this.call.distance(argument.call) == 0.0;
        }

        boolean sameName = this.name.equalsValue(argument.getName());

        return sameName && sameCall ? 0.0 : 1.0;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        if(other == this){
            return Collections.emptyList();
        }

        if(!Argument.class.isAssignableFrom(other.getClass())){
            return Collections.singletonList(Action.addElement(this.getClass(), this));
        }

        if(this.name.equalsValue(((Argument)other).name)){
            return Collections.emptyList();
        }

        return Collections.singletonList(Action.changeStepArgument(this, other));
    }

    @Override
    public Token getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return call != null ? call.toString() : name.toString();
    }

    @Override
    public void setSourceFile(SourceFile sourceFile) {
        super.setSourceFile(sourceFile);

        if(call != null){
            this.call.setSourceFile(sourceFile);
        }
    }
}
