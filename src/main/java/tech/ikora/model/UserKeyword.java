package tech.ikora.model;


import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.exception.InvalidTypeException;
import tech.ikora.types.*;

import java.util.List;

public class UserKeyword extends KeywordDefinition {
    private NodeList<Variable> arguments;
    private NodeList<Value> returnVariables;

    private KeywordCall tearDown;

    public UserKeyword(Token name) {
        super(name);
        this.arguments = new NodeList<>(Token.empty());
        this.returnVariables = new NodeList<>(Token.empty());
    }

    public void setArgumentList(NodeList<Variable> arguments){
        this.arguments = arguments;
        this.addAstChild(this.arguments);
        addTokens(arguments.getTokens());
    }

    public void setReturnVariables(NodeList<Value> returnVariables){
        this.returnVariables = returnVariables;
        this.addAstChild(this.returnVariables);
        addTokens(returnVariables.getTokens());
    }

    public void setTearDown(KeywordCall tearDown) {
        this.tearDown = tearDown;
        this.addAstChild(this.tearDown);
    }

    public void setTearDown(Step tearDown) throws InvalidTypeException {
        setTearDown(tearDown.toCall());
    }

    @Override
    public void addStep(Step step) throws Exception {
        super.addStep(step);

        if(Assignment.class.isAssignableFrom(step.getClass())){
            addLocalVariables(((Assignment) step).getLeftHandOperand());
        }
    }

    @Override
    public NodeList<Value> getReturnValues() {
        return returnVariables;
    }

    @Override
    public BaseTypeList getArgumentTypes() {
        return new BaseTypeList(arguments.stream()
                .map(BaseTypeFactory::fromVariable)
                .toArray(BaseType[]::new));
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public List<Variable> getLocalVariables() {
        List<Variable> localVariables = super.getLocalVariables();
        localVariables.addAll(arguments);

        return localVariables;
    }
}
