package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.runner.Runtime;

import java.util.Collections;
import java.util.List;

public class Literal extends Value {
    private Token name;
    private List<Variable> variables;

    public Literal(Token name) {
        this.name = name;
        this.variables = Collections.emptyList();

        addToken(this.name);
    }

    public Literal(Token name, List<Variable> variables) {
        this.name = name;
        this.variables = variables;

        addToken(this.name);
    }

    public List<Variable> getVariables() {
        return this.variables;
    }

    @Override
    public boolean matches(Token name) {
        return false;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {

    }

    @Override
    public void execute(Runtime runtime) throws Exception {

    }

    @Override
    public Token getNameToken() {
        return this.name;
    }

    @Override
    public double distance(Differentiable other) {
        if(this == other){
            return 0.;
        }

        if(other == null){
            return 1.;
        }

        if(Literal.class.isAssignableFrom(other.getClass())){
            return this.name.equalsIgnorePosition(((Literal)other).name) ? 0. : 1.;
        }

        return 1.;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        return null;
    }
}
