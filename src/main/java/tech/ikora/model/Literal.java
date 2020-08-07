package tech.ikora.model;

import tech.ikora.analytics.Edit;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.runner.Runtime;

import java.util.Collections;
import java.util.List;

public class Literal extends Value {
    private final Token name;
    private final List<Variable> variables;

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
    public double distance(SourceNode other) {
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
    public List<Edit> differences(SourceNode other) {
        if(other == null){
            throw new NullPointerException("Cannot find differences between element and null");
        }

        if(other instanceof EmptyNode){
            return Collections.singletonList(Edit.removeElement(this.getClass(), this, (EmptyNode)other));
        }

        if(other == this){
            return Collections.emptyList();
        }

        if(!(other instanceof Literal)){
            return Collections.singletonList(Edit.changeValueType(this, other));
        }

        Literal literal = (Literal)other;
        if(!this.getName().equals(literal.getName())){
            return Collections.singletonList(Edit.changeValueName(this, other));
        }

        return Collections.emptyList();
    }
}
