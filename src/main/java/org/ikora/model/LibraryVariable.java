package org.ikora.model;

import org.ikora.analytics.Action;
import org.ikora.analytics.visitor.NodeVisitor;
import org.ikora.analytics.visitor.VisitorMemory;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public abstract class LibraryVariable extends Variable {
    protected enum Format{
        SCALAR, LIST, DICTIONARY
    }

    protected Format format;

    public LibraryVariable(){
        super("");
        this.format = Format.SCALAR;
        setName(toVariable(this.getClass()));
    }

    @Override
    protected void setName(String name){
        this.name = name;

        String patternString = Value.escape(getName());
        patternString = Value.getGenericVariableName(patternString);
        this.pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public String getValueAsString() {
        return "";
    }

    @Override
    public void addElement(String element) {

    }

    @Override
    public List<Value> getValues() {
        return null;
    }

    @Override
    public boolean isDeadCode(){
        return false;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {

    }

    @Override
    public double distance(Differentiable other) {
        return this.differences(other).isEmpty() ? 0 : 1;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        if(this != other){
            return Collections.singletonList(Action.changeVariableDefinition(this, other));
        }

        return Collections.emptyList();
    }

    private static String toVariable(Class<? extends LibraryVariable> variableClass) {
        return String.format("${%s}", variableClass.getSimpleName());
    }
}
