package org.ikora.model;

import org.ikora.analytics.Action;
import org.ikora.exception.InvalidDependencyException;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Value implements Differentiable {


    private Node parent;
    private Token token;
    private Map<Token, Set<Variable>> variables;

    public Value(Node parent, Token token) {
        this.parent = parent;
        this.token = token;
        this.variables = new HashMap<>();
    }

    public Value(Token token){
        this(null, token);
    }

    public Token getToken(){
        return token;
    }

    public void setVariable(Token name, Variable variable) throws InvalidDependencyException {
        if(variable == null){
            return;
        }

        this.variables.putIfAbsent(name, new HashSet<>());
        Set<Variable> variables = this.variables.get(name);
        variables.add(variable);

        if(parent != null){
            variable.addDependency(parent);
        }
    }

    public void setVariable(Token name, Set<Variable> variables) throws InvalidDependencyException {
        for(Variable variable: variables){
            setVariable(name, variable);
        }
    }

    @Override
    public String toString() {
        return this.token.getText();
    }

    @Override
    public double distance(Differentiable other) {
        if(other == null){
            return 1.0;
        }

        if(other == this){
            return 0.0;
        }

        if(!(other instanceof Value)){
            return 1.0;
        }

        Value value = (Value)other;
        return this.token.equals(value.token) ? 0 : 1;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        if(other == this){
            return Collections.emptyList();
        }

        if(!Value.class.isAssignableFrom(other.getClass())){
            return Collections.singletonList(Action.addElement(Value.class, this));
        }

        Value value = (Value)other;

        if(value.token.equalsValue(this.token)){
            return Collections.emptyList();
        }

        return Collections.singletonList(Action.addElement(Value.class, this));
    }

    public static Value empty(){
        return new Value(Token.empty());
    }
}
