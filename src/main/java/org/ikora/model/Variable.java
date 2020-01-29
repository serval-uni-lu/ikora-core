package org.ikora.model;

import org.ikora.builder.VariableParser;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.exception.MalformedVariableException;
import org.ikora.runner.Runtime;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Variable extends Node {
    private Assignment assignment;

    protected Token name;
    protected Pattern pattern;

    public Variable(Token name) {
        this.assignment = null;
        setName(name);
    }

    public static Variable invalid() {
        return new InvalidVariable();
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public boolean isAssignment(){
        return this.assignment != null;
    }

    @Override
    public Value getNameAsValue() {
        return new Value(this.name);
    }

    @Override
    public Token getName() {
        return this.name;
    }

    @Override
    public boolean matches(Token name) {
        String generic = Value.getGenericVariableName(name.getValue());

        Matcher matcher = pattern.matcher(generic);
        return matcher.matches();
    }

    public static Variable create(Value value) throws MalformedVariableException, InvalidDependencyException {
        Optional<Variable> variable = VariableParser.parse(value.getName());

        if(variable.isPresent()){
            value.setVariable(value.getName(), variable.get());
        }
        else{
            throw new MalformedVariableException(String.format("Failed to create variable from value '%s'", value.getName()));
        }

        return variable.get();
    }

    @Override
    public void execute(Runtime runtime) {
        // nothing to do for variables
    }

    protected abstract void setName(Token name);
    public abstract void addElement(Token element);
    public abstract List<Value> getValues();
}
