package org.ikora.model;

import org.ikora.builder.VariableParser;
import org.ikora.runner.Runtime;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Variable extends Node {
    public enum Type{
        SCALAR, LIST, DICTIONARY
    }

    private Assignment assignment;

    protected String name;
    protected Pattern pattern;

    public Variable(String name) {
        this.assignment = null;
        setName(name);
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
    public String getName() {
        return this.name;
    }

    public Optional<List<Value>> getResolvedValues() {
        List<Value> values = new ArrayList<>();

        for(Value value: getValues()){
            Optional<List<Value>> resolvedValues = value.getResolvedValues();

            if(!resolvedValues.isPresent()){
                return Optional.empty();
            }

            values.addAll(resolvedValues.get());
        }

        return Optional.of(values);
    }

    @Override
    public boolean matches(String name) {
        String generic = Value.getGenericVariableName(name);

        Matcher matcher = pattern.matcher(generic);
        return matcher.matches();
    }

    public static Variable create(Value value) throws Exception {
        Optional<Variable> variable = VariableParser.parse(value.toString());

        if(variable.isPresent()){
            value.setVariable(value.toString(), variable.get());
        }
        else{
            throw new Exception(String.format("Failed to create variable from value '%s'", value.getName()));
        }

        return variable.get();
    }

    @Override
    public void execute(Runtime runtime) {
        // nothing to do for variables
    }

    protected abstract void setName(String name);
    public abstract void addElement(String element);
    public abstract List<Value> getValues();
    public abstract String getValueAsString();
}
