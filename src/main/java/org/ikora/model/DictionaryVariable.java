package org.ikora.model;

import org.ikora.analytics.Action;
import org.ikora.analytics.visitor.NodeVisitor;
import org.ikora.analytics.visitor.VisitorMemory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DictionaryVariable extends Variable {
    private List<Value> values;

    public DictionaryVariable(Token name){
        super(name);
        this.values = new ArrayList<>();
    }

    @Override
    public double distance(Differentiable other) {
        return 0;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        return null;
    }

    @Override
    public void addElement(Token element) {
        values.add(new Value(this, element));
    }

    @Override
    public String toString() {
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
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    protected void setName(Token name) {
        this.name = name;
        String generic = Value.escape(Value.getGenericVariableName(this.name.getText()));

        this.pattern = Pattern.compile(generic, Pattern.CASE_INSENSITIVE);
    }
}
