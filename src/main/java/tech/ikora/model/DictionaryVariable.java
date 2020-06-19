package tech.ikora.model;

import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.ValueResolver;
import tech.ikora.exception.InvalidArgumentException;

import java.util.regex.Pattern;

public class DictionaryVariable extends Variable {

    public DictionaryVariable(Token name){
        super(name);
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

        String generic = ValueResolver.escape(ValueResolver.getGenericVariableName(this.name.getText()));
        this.pattern = Pattern.compile(generic, Pattern.CASE_INSENSITIVE);
    }
}
