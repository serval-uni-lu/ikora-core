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
    private NodeList<Variable> parameters;
    private NodeList<Value> returnVariables;

    private KeywordCall tearDown;

    public UserKeyword(Token name) {
        super(name);

        this.parameters = new NodeList<>(Token.empty());
        this.addAstChild(this.parameters);

        this.returnVariables = new NodeList<>(Token.empty());
        this.addAstChild(this.returnVariables);
    }

    public NodeList<Variable> getParameters() {
        return parameters;
    }

    public NodeList<Value> getReturnVariables() {
        return returnVariables;
    }

    public KeywordCall getTearDown() {
        return tearDown;
    }

    public Optional<Variable> getParameterByName(Token name) {
        return parameters.stream().filter(a -> a.matches(name)).findFirst();
    }

    public void setParameters(NodeList<Variable> arguments){
        this.parameters = arguments;
        this.addAstChild(this.parameters);
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
    public void addStep(Step step) {
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
        return new BaseTypeList(parameters.stream()
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

        if(parameters.stream().anyMatch(v -> v.matches(variable.getNameToken()))){
            definitions.add(this);
        }

        return definitions;
    }
}
