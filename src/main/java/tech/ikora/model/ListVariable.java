package tech.ikora.model;

import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.ValueResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ListVariable extends Variable {
    public ListVariable(Token name){
        super(name);
    }

    public void setValues(List<SourceNode> values){
        this.values = values;
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
    protected void setName(Token name){
        this.name = name;
        String generic = ValueResolver.getGenericVariableName(this.name.getText());
        String bareName = ValueResolver.escape(ValueResolver.getBareVariableName(generic));

        String patternString = String.format("^[\\$@]\\{%s(\\[\\d+\\])*}$", bareName);
        this.pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
    }
}
