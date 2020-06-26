package tech.ikora.model;

import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.exception.InvalidTypeException;
import tech.ikora.types.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserKeyword extends KeywordDefinition {
    private final SourceNodeTable<Variable> parameters;
    private final BaseTypeList parameterTypes;
    private final SourceNodeTable<Variable> embeddedVariables;
    private final List<Variable> returnVariables;

    private KeywordCall tearDown;

    public UserKeyword(Token name) {
        super(name);

        parameters = new SourceNodeTable<>();
        embeddedVariables = new SourceNodeTable<>();

        parameterTypes = new BaseTypeList();

        returnVariables = new ArrayList<>();
    }

    public KeywordCall getTearDown() {
        return tearDown;
    }

    public void setTearDown(KeywordCall tearDown) {
        this.tearDown = tearDown;
        this.addAstChild(this.tearDown);
    }

    public void setTearDown(Step tearDown) throws InvalidTypeException {
        setTearDown(tearDown.toCall());
    }

    public void addReturnVariable(Variable returnVariable) {
        returnVariables.add(returnVariable);
        this.addAstChild(returnVariable);
    }

    @Override
    public void addStep(Step step) throws Exception {
        super.addStep(step);

        if(Assignment.class.isAssignableFrom(step.getClass())){
            for (Variable variable: ((Assignment)step).getLeftHandOperand()){
                localVariables.add(variable);
            }
        }
    }

    @Override
    public List<Variable> getReturnVariables() {
        return returnVariables;
    }

    @Override
    public BaseTypeList getArgumentTypes() {
        return parameterTypes;
    }

    public void addExplicitParameter(Variable parameter){
        parameters.add(parameter);
        addParameter(parameter);
    }

    public void addEmbeddedVariable(Variable embeddedVariable) {
        embeddedVariables.add(embeddedVariable);
        addParameter(embeddedVariable);
    }

    public SourceNodeTable<Variable> getParameters() {
        return parameters;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    private void addParameter(Variable parameter){
        localVariables.add(parameter);
        this.addAstChild(parameter);

        parameterTypes.add(BaseTypeFactory.fromVariable(parameter));
    }

    @Override
    public SourceNodeTable<Variable> getLocalVariables() {
        SourceNodeTable<Variable> localVariables = super.getLocalVariables();
        localVariables.addAll(parameters);
        localVariables.addAll(embeddedVariables);

        return localVariables;
    }
}
