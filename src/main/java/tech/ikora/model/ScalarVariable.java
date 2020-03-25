package tech.ikora.model;

import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.ValueLinker;
import tech.ikora.exception.InvalidArgumentException;

import java.util.*;
import java.util.regex.Pattern;

public class ScalarVariable extends Variable {

    public ScalarVariable(Token name){
        super(name);
    }

    @Override
    public void addValue(Node value) throws InvalidArgumentException {
        if(!values.isEmpty()){
            throw new InvalidArgumentException("Scalar variable only accept one element");
        }

        this.values.add(value);
    }

    public Optional<Node> getValue() {
        if(values.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(values.get(0));
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
        String generic = ValueLinker.getGenericVariableName(this.name.getText());
        String bareName = ValueLinker.escape(ValueLinker.getBareVariableName(generic));

        String patternString = String.format("^\\$\\{%s(((\\[\\d+\\])*)|([\\+\\-\\*/]\\d+))}$", bareName);
        this.pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
    }
}
