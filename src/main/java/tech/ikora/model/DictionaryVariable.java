package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.ValueLinker;
import tech.ikora.exception.InvalidArgumentException;

import java.util.List;
import java.util.regex.Pattern;

public class DictionaryVariable extends Variable {

    public DictionaryVariable(Token name){
        super(name);
    }

    @Override
    public void addValue(Node value) throws InvalidArgumentException {
        if(!Variable.class.isAssignableFrom(value.getClass()) && !DictionaryEntry.class.isAssignableFrom(value.getClass())){
            throw new InvalidArgumentException("Key value pair or variable expected");
        }

        values.add(value);
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

        String generic = ValueLinker.escape(ValueLinker.getGenericVariableName(this.name.getText()));
        this.pattern = Pattern.compile(generic, Pattern.CASE_INSENSITIVE);
    }
}
