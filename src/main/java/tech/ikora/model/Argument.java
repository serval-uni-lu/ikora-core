package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.ValueLinker;
import tech.ikora.exception.InvalidDependencyException;
import tech.ikora.runner.Runtime;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Argument extends Node {
    public enum Type{
        STRING, OBJECT, KEYWORD, LOCATOR, CONDITION, KEYWORDS, KWARGS
    }

    private Node definition;
    private final Token name;

    public Argument(Node parent, Token name) throws InvalidDependencyException {
        this.name = name;
        this.definition = null;

        this.addDependency(parent);
        this.addToken(name);
    }

    public Argument(Node parent, Node definition) throws InvalidDependencyException {
        this.addDependency(parent);

        if(definition == null){
            this.name = Token.empty();
            this.definition = null;
            return;
        }

        this.name = definition.getName();
        this.definition = definition;
        addTokens(this.definition.getTokens());

        definition.addDependency(this);
    }

    public Optional<Node> getDefinition() {
        return Optional.ofNullable(this.definition);
    }

    public void setCall(KeywordCall call){
        this.definition = call;
        this.definition.setSourceFile(getSourceFile());
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
        if(this.definition != null && this.definition != argument.definition){
            sameCall = this.definition.distance(argument.definition) == 0.0;
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
        if(name != null){
            return name.toString();
        }

        return definition != null ? definition.toString() : "<ARGUMENT>";
    }

    @Override
    public void setSourceFile(SourceFile sourceFile) {
        super.setSourceFile(sourceFile);

        if(definition != null){
            this.definition.setSourceFile(sourceFile);
        }
    }
}
