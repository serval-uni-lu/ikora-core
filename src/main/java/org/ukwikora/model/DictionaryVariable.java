package org.ukwikora.model;

import org.ukwikora.analytics.Action;
import org.ukwikora.analytics.VisitorMemory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DictionaryVariable extends Variable {
    private List<Value> values;

    public DictionaryVariable(String name){
        super(name);
        this.values = new ArrayList<>();
    }

    @Override
    public double distance(@Nonnull Differentiable other) {
        return 0;
    }

    @Override
    public List<Action> differences(@Nonnull Differentiable other) {
        return null;
    }

    @Override
    public void addElement(String element) {
        values.add(new Value(this, element));
    }

    @Override
    public String getValueAsString() {
        return values.stream().map(Value::toString).collect(Collectors.joining("\t"));
    }

    @Override
    public List<Value> getValues() {
        return values;
    }

    @Override
    public boolean isDeadCode(){
        return getDependencies().size() == 0;
    }

    @Override
    public void accept(StatementVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    protected void setName(String name) {
        this.name = name;
        String generic = Value.escape(Value.getGenericVariableName(this.name));

        this.pattern = Pattern.compile(generic, Pattern.CASE_INSENSITIVE);
    }
}
