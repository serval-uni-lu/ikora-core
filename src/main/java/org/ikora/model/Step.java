package org.ikora.model;

import org.ikora.exception.InvalidDependencyException;

import java.util.*;


public abstract class Step extends Node {
    private Value name;

    public Step(String name) {
        this.name = new Value(name);
    }

    public Value getNameAsValue() {
        return this.name;
    }

    public String getName() {
        return this.name.toString();
    }

    public Keyword getParent() throws InvalidDependencyException {
        Set<Node> parents = getDependencies();

        if(parents.isEmpty()){
            throw new InvalidDependencyException("No parent found");
        }

        if(parents.size() > 1){
            throw new InvalidDependencyException("Too many parent found");
        }

        return (Keyword)parents.iterator().next();
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Step)) {
            return false;
        }

        Step step = (Step)other;

        return name.equals(step.name);
    }

    @Override
    public boolean matches(String name){
        return getName().matches(name);
    }

    public abstract Optional<KeywordCall> getKeywordCall();
    public abstract List<Argument> getArgumentList();
    public abstract boolean hasParameters();
}
