package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.ValueResolver;
import tech.ikora.runner.Runtime;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Argument extends SourceNode {
    private SourceNode definition;
    private final Token name;

    public Argument(SourceNode definition) {
        if(definition == null){
            this.name = Token.empty();
            this.definition = null;
            return;
        }

        this.name = definition.getNameToken();
        this.definition = definition;
        addTokens(this.definition.getTokens());

        this.addAstChild(this.definition);
    }

    public Optional<SourceNode> getDefinition() {
        return Optional.ofNullable(this.definition);
    }

    @Override
    public boolean matches(Token name) {
        return ValueResolver.matches(this.name, name);
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
        if(this.definition != null && this.definition != argument.definition){
            sameCall = this.definition.distance(argument.definition) == 0.0;
        }

        boolean sameName = this.name.equalsIgnorePosition(argument.getNameToken());

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

        if(this.name.equalsIgnorePosition(((Argument)other).name)){
            return Collections.emptyList();
        }

        return Collections.singletonList(Action.changeStepArgument(this, other));
    }

    @Override
    public Token getNameToken() {
        return this.name;
    }

    @Override
    public String toString() {
        if(name != null){
            return name.toString();
        }

        return "<ARGUMENT>";
    }
}
