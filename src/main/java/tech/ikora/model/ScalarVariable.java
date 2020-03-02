package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.ValueLinker;
import tech.ikora.exception.InvalidArgumentException;
import tech.ikora.utils.LevenshteinDistance;

import java.util.*;
import java.util.regex.Pattern;

public class ScalarVariable extends Variable {
    private Node value;

    public ScalarVariable(Token name){
        super(name);
        this.value = null;
    }

    @Override
    public void addElement(Node value) throws InvalidArgumentException {
        if(this.value != null){
            throw new InvalidArgumentException("Scalar variable only accept one element");
        }

        setValue(value);
    }

    public void setValue(Node value) {
        this.value = value;
    }

    public Node getValue() {
        return value;
    }

    @Override
    public boolean isDeadCode(){
        return getDependencies().size() == 0;
    }

    @Override
    public double distance(Differentiable other) {
        if(!(other instanceof ScalarVariable)){
            return 1;
        }

        ScalarVariable variable = (ScalarVariable)other;
        boolean same = getName().equalsIgnorePosition(variable.getName());

        if(this.value != null){
            same &= this.value.distance(variable.value) == 0.0;
        }

        return same ? 0.0 : 1.0;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(!(other instanceof ScalarVariable)){
            actions.add(Action.invalid(this, other));
            return actions;
        }

        ScalarVariable variable = (ScalarVariable)other;

        if(!getName().equalsIgnorePosition(variable.getName())){
            actions.add(Action.changeName(this, variable));
        }

        if(this.value != null){
            actions.addAll(this.value.differences(variable.value));
        }

        return actions;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    protected void setName(Token name) {
        this.name = name;
        String generic = ValueLinker.getGenericVariableName(this.name.getText());
        String bareName = ValueLinker.escape(ValueLinker.getBareVariableName(generic));

        String patternString = String.format("^\\$\\{%s(((\\[\\d+\\])*)|([\\+\\-\\*/]\\d+))}$", bareName);
        this.pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
    }
}
