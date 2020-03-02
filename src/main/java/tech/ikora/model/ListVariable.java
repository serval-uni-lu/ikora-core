package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.ValueLinker;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ListVariable extends Variable {
    private List<Node> values;

    public ListVariable(Token name){
        super(name);
        this.values = new ArrayList<>();
    }

    @Override
    public void addElement(Node value) {
        if(this.values == null){
            this.values = new ArrayList<>();
        }

        this.values.add(value);
    }

    public void setValues(List<Node> values){
        this.values = values;
    }

    @Override
    public boolean isDeadCode(){
        return getDependencies().size() == 0;
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
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name.getText());

        for(Node value: values){
            builder.append("\t");
            builder.append(value.getName());
        }

        return builder.toString();
    }

    @Override
    protected void setName(Token name){
        this.name = name;
        String generic = ValueLinker.getGenericVariableName(this.name.getText());
        String bareName = ValueLinker.escape(ValueLinker.getBareVariableName(generic));

        String patternString = String.format("^[\\$@]\\{%s(\\[\\d+\\])*}$", bareName);
        this.pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
    }
}
