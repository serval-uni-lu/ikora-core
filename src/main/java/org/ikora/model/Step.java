package org.ikora.model;

import org.ikora.exception.InvalidDependencyException;

import javax.annotation.Nonnull;
import java.util.*;


public abstract class Step extends Node {
    private Value name;

    public void setName(@Nonnull String name) {
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
    public boolean matches(@Nonnull String name){
        return getName().matches(name);
    }

    @Override
    public void addDependency(Node dependency) throws InvalidDependencyException{
        if(!Keyword.class.isAssignableFrom(dependency.getClass())){
            throw new InvalidDependencyException("steps always have their parent as dependency");
        }

        super.addDependency(dependency);
    }

    public abstract Optional<KeywordCall> getKeywordCall();
    public abstract List<Argument> getArgumentList();
    public abstract boolean hasParameters();
}
