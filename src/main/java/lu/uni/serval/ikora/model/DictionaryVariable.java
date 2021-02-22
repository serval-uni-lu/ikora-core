package lu.uni.serval.ikora.model;

import lu.uni.serval.ikora.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.builder.ValueResolver;

import java.util.regex.Pattern;

public class DictionaryVariable extends Variable {

    public DictionaryVariable(Token name){
        super(name);
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
