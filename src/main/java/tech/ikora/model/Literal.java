package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.runner.Runtime;

import java.util.List;

public class Literal extends SourceNode {
    private Token name;

    public Literal(Token name) {
        this.name = name;
        addToken(this.name);
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
