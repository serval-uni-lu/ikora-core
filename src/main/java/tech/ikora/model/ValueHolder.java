package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.runner.Runtime;

import java.util.Collections;
import java.util.List;

public class ValueHolder extends Node {
    private Literal literal;
    private Variable variable;

    public ValueHolder(Literal literal){
        if(literal == null){
            throw new NullPointerException();
        }

        this.literal = literal;
        variable = null;
    }

    public ValueHolder(Variable variable){
        if(variable == null){
            throw new NullPointerException();
        }

        this.literal = null;
        this.variable = variable;
    }

    public boolean isVariable(){
        return this.variable != null;
    }

    public boolean isLiteral(){
        return this.literal != null;
    }


    @Override
    public boolean matches(Token name) {
        if(isVariable()){
            return this.variable.matches(name);
        }

        if(isLiteral()){
            return this.literal.matches(name);
        }

        throw new NullPointerException("ValueHolder cannot have both literal and value set to NULL");
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        if(isVariable()){
            this.variable.accept(visitor, memory);
        }

        if(isLiteral()){
            this.literal.accept(visitor, memory);
        }

        throw new NullPointerException("ValueHolder cannot have both literal and value set to NULL");
    }

    @Override
    public void execute(Runtime runtime) throws Exception {
        if(isVariable()){
            this.variable.execute(runtime);
        }

        if(isLiteral()){
            this.literal.execute(runtime);
        }

        throw new NullPointerException("ValueHolder cannot have both literal and value set to NULL");
    }

    @Override
    public Token getName() {
        if(isVariable()){
            return this.variable.getName();
        }

        if(isLiteral()){
            return this.literal.getName();
        }

        throw new NullPointerException("ValueHolder cannot have both literal and value set to NULL");
    }

    @Override
    public double distance(Differentiable other) {
        if(this == other){
            return 1.;
        }

        if(this.isVariable()){
            return this.variable.distance(other);
        }

        if(this.isLiteral()){
            return this.literal.distance(other);
        }

        throw new NullPointerException("ValueHolder cannot have both literal and value set to NULL");
    }

    @Override
    public List<Action> differences(Differentiable other) {
        if(this == other){
            return Collections.emptyList();
        }

        if(this.isVariable()){
            return this.variable.differences(other);
        }

        if(this.isLiteral()){
            return this.literal.differences(other);
        }

        throw new NullPointerException("ValueHolder cannot have both literal and value set to NULL");
    }
}
