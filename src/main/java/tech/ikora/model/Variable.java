package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.builder.ValueResolver;
import tech.ikora.builder.VariableParser;
import tech.ikora.exception.InvalidArgumentException;
import tech.ikora.exception.MalformedVariableException;
import tech.ikora.runner.Runtime;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Variable extends Value {
    protected Token name;
    protected Pattern pattern;
    protected Link<Variable, Node> link;

    public Variable(Token name) {
        setName(name);

        this.name.setType(Token.Type.VARIABLE);
        this.link = new Link<>(this);
    }

    public static Variable invalid() {
        return new InvalidVariable();
    }

    public void linkToDefinition(Node definition, Link.Import link){
        this.link.addNode(definition, link);
    }

    public Set<Node> getDefinition(Link.Import linkType){
        return this.link.getAllLinks(linkType);
    }

    @Override
    public Token getNameToken() {
        return this.name;
    }

    @Override
    public boolean matches(Token name) {
        String generic = ValueResolver.getGenericVariableName(name.getText());

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

        return this.getNameToken().equalsIgnorePosition(((Variable)other).getNameToken()) ? 0 : 1;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        if(other == null){
            return Collections.singletonList(Action.removeElement(this.getClass(), this));
        }

        if(this.getClass() != other.getClass()){
            return Collections.singletonList(Action.changeType(this, other));
        }

        if(!this.getNameToken().equalsIgnorePosition(((Variable)other).getNameToken())){
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
        return name.getText();
    }

    protected abstract void setName(Token name);
}
