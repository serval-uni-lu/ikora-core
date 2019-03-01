package org.ukwikora.model;

import org.ukwikora.analytics.Action;
import org.ukwikora.analytics.VisitorMemory;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public abstract class LibraryVariable extends Variable {
    protected enum Format{
        scalar, list, dictionary
    }

    protected Format format;

    public LibraryVariable(){
        this.format = Format.scalar;
        setName(toVariable(this.getClass()));
    }

    @Override
    protected void setName(String name){
        this.name = name;

        String patternString = Value.escape(getName());
        patternString = patternString.replaceAll("\\s", "(\\\\s|_)");
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
    public void accept(StatementVisitor visitor, VisitorMemory memory) {

    }

    @Override
    public double distance(@Nonnull Differentiable other) {
        return this.differences(other).isEmpty() ? 0 : 1;
    }

    @Override
    public List<Action> differences(@Nonnull Differentiable other) {
        if(this != other){
            return Collections.singletonList(Action.changeVariableDefinition(this, other));
        }

        return Collections.emptyList();
    }

    private static String toVariable(Class<? extends LibraryVariable> variableClass) {
        String name = variableClass.getSimpleName();
        name = name.replaceAll("([A-Z])", " $1").trim().toUpperCase();

        return String.format("${%s}", name);
    }
}
