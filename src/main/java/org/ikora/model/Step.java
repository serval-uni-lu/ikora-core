package org.ikora.model;

import org.ikora.exception.InvalidDependencyException;
import org.ikora.exception.InvalidTypeException;

import java.util.*;

public abstract class Step extends Node {
    private Value name;
    protected KeywordCall template;

    public Step(Token name) {
        this.name = new Value(name);
    }

    protected void setName(Token name){
        this.name = new Value(name);
    }

    public Value getNameAsValue() {
        return this.name;
    }

    public Token getName() {
        return this.name.getName();
    }

    public Node getParent() throws InvalidDependencyException {
        Set<Node> parents = getDependencies();

        if(parents.isEmpty()){
            throw new InvalidDependencyException("No parent found");
        }

        if(parents.size() > 1){
            throw new InvalidDependencyException("Too many parent found");
        }

        return parents.iterator().next();
    }

    public KeywordDefinition getCaller() throws InvalidDependencyException {
        Node node = getParent();

        while (Step.class.isAssignableFrom(node.getClass())){
            node = ((Step)node).getParent();
        }

        if(node instanceof KeywordDefinition){
            return (KeywordDefinition)node;
        }

        throw new InvalidDependencyException("Step should always have a keyword definition caller");
    }

    @Override
    public boolean matches(Token name){
        return this.name.matches(name);
    }

    public abstract Optional<KeywordCall> getKeywordCall();
    public abstract List<Argument> getArgumentList();
    public abstract boolean hasParameters();

    public void setTemplate(KeywordCall template) throws InvalidDependencyException {
        this.template = template;
    }

    public KeywordCall toCall() throws InvalidTypeException {
        if(KeywordCall.class.isAssignableFrom(this.getClass())){
            return (KeywordCall) this;
        }

        throw new InvalidTypeException(String.format("Expected a keyword call got %s instead", this.getClass().getName()));
    }
}
