package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.builder.ValueLinker;
import tech.ikora.builder.VariableParser;
import tech.ikora.exception.InvalidArgumentException;
import tech.ikora.exception.MalformedVariableException;
import tech.ikora.runner.Runtime;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Variable extends Node {
    protected Token name;
    protected Pattern pattern;
    protected List<Node> values;

    public Variable(Token name) {
        setName(name);

        this.name.setType(Token.Type.VARIABLE);
        this.values = new ArrayList<>();
    }

    public List<Node> getValues(){
        return values;
    }

    public static Variable invalid() {
        return new InvalidVariable();
    }

    @Override
    public Token getName() {
        return this.name;
    }

    @Override
    public boolean matches(Token name) {
        String generic = ValueLinker.getGenericVariableName(name.getText());

        Matcher matcher = pattern.matcher(generic);
        return matcher.matches();
    }

    @Override
    public double distance(Differentiable other) {
        if(other == null){
            return 1;
        }

        if(this.getClass() != other.getClass()){
            return 1;
        }

        return this.getName().equalsIgnorePosition(((Variable)other).getName()) ? 0 : 1;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        if(other == null){
            return Collections.singletonList(Action.removeElement(this.getClass(), this));
        }

        if(this.getClass() != other.getClass()){
            return Collections.singletonList(Action.changeType(this, other));
        }

        if(!this.getName().equalsIgnorePosition(((Variable)other).getName())){
            return Collections.singletonList(Action.changeName(this, other));
        }

        return Collections.emptyList();
    }

    public static Variable create(Token token) throws MalformedVariableException {
        Optional<Variable> variable = VariableParser.parse(token);

        if(!variable.isPresent()){
            throw new MalformedVariableException(String.format("Failed to create variable from value '%s'", token));
        }

        return variable.get();
    }

    @Override
    public void execute(Runtime runtime) {
        // nothing to do for variables
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name.getText());

        for(Node value: values){
            builder.append("\t");
            builder.append(value.toString());
        }

        return builder.toString();
    }

    protected abstract void setName(Token name);
    public abstract void addValue(Node value) throws InvalidArgumentException;

}
