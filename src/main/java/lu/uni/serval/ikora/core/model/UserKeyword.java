package lu.uni.serval.ikora.core.model;


import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.exception.InvalidTypeException;
import lu.uni.serval.ikora.core.types.BaseType;
import lu.uni.serval.ikora.core.types.BaseTypeFactory;
import lu.uni.serval.ikora.core.types.BaseTypeList;

import java.util.List;
import java.util.Optional;

public class UserKeyword extends KeywordDefinition {
    private NodeList<Variable> arguments;
    private NodeList<Value> returnVariables;

    private KeywordCall tearDown;

    public UserKeyword(Token name) {
        super(name);

        this.arguments = new NodeList<>(Token.empty());
        this.addAstChild(this.arguments);

        this.returnVariables = new NodeList<>(Token.empty());
        this.addAstChild(this.returnVariables);
    }

    public NodeList<Variable> getArguments() {
        return arguments;
    }

    public NodeList<Value> getReturnVariables() {
        return returnVariables;
    }

    public KeywordCall getTearDown() {
        return tearDown;
    }

    public Optional<Variable> getParameterByName(Token name) {
        return arguments.stream().filter(a -> a.matches(name)).findFirst();
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
    public List<Dependable> findDefinition(Variable variable) {
        final List<Dependable> definitions = super.findDefinition(variable);

        if(arguments.stream().anyMatch(v -> v.matches(variable.getNameToken()))){
            definitions.add(this);
        }

        return definitions;
    }
}
