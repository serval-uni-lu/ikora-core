package tech.ikora.model;

import tech.ikora.builder.ValueResolver;
import tech.ikora.exception.InvalidDependencyException;
import tech.ikora.exception.InvalidTypeException;

import java.util.*;

public abstract class Step extends SourceNode {
    private Token name;
    protected KeywordCall template;

    public Step(Token name) {
        setName(name);
    }

    protected void setName(Token name){
        this.name = name;
    }

    public Token getNameToken() {
        return this.name;
    }

    public List<Step> getSteps(){
        return Collections.emptyList();
    }

    public KeywordDefinition getCaller() throws InvalidDependencyException {
        SourceNode sourceNode = getAstParent();

        while (Argument.class.isAssignableFrom(sourceNode.getClass()) || Step.class.isAssignableFrom(sourceNode.getClass())){
            sourceNode = sourceNode.getAstParent();
        }

        if(sourceNode instanceof KeywordDefinition){
            return (KeywordDefinition) sourceNode;
        }

        throw new InvalidDependencyException("Step should always have a keyword definition caller");
    }

    @Override
    public boolean matches(Token name){
        return ValueResolver.matches(this.name, name);
    }

    public abstract Optional<KeywordCall> getKeywordCall();
    public abstract ArgumentList getArgumentList();
    public abstract boolean hasParameters();

    public void setTemplate(KeywordCall template) {
        this.template = template;
        this.template.addDependency(this);
    }

    public KeywordCall toCall() throws InvalidTypeException {
        if(KeywordCall.class.isAssignableFrom(this.getClass())){
            return (KeywordCall) this;
        }

        throw new InvalidTypeException(String.format("Expected a keyword call got %s instead", this.getClass().getName()));
    }
}
