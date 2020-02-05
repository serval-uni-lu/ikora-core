package tech.ikora.model;

import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.exception.InvalidTypeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserKeyword extends KeywordDefinition {
    private List<Variable> parameters;
    private List<Variable> embeddedVariables;
    private NodeTable<Variable> localVariables;

    private KeywordCall tearDown;
    private List<Variable> returnVariables;

    public UserKeyword() {
        parameters = new ArrayList<>();
        embeddedVariables = new ArrayList<>();

        localVariables = new NodeTable<>();
        returnVariables = new ArrayList<>();
    }

    public KeywordCall getTearDown() {
        return tearDown;
    }

    public void setTearDown(KeywordCall tearDown) {
        this.tearDown = tearDown;
    }

    public void setTearDown(Step tearDown) throws InvalidTypeException {
        setTearDown(tearDown.toCall());
    }

    public void addReturnVariable(Variable returnVariable) {
        returnVariables.add(returnVariable);
    }

    @Override
    public void setName(Token name) {
        super.setName(name);
    }

    @Override
    public void addStep(Step step) throws Exception {
        super.addStep(step);

        if(Assignment.class.isAssignableFrom(step.getClass())){
            for (Variable variable: ((Assignment)step).getReturnVariables()){
                localVariables.add(variable);
            }
        }
    }

    @Override
    public List<Variable> getReturnVariables() {
        return returnVariables;
    }

    public void addParameter(Variable parameter){
        parameters.add(parameter);
        localVariables.add(parameter);
    }

    public void addEmbeddedVariable(Variable embeddedVariable) {
        embeddedVariables.add(embeddedVariable);
        localVariables.add(embeddedVariable);
    }

    public List<Variable> getParameters() {
        return parameters;
    }

    public Set<Variable> findLocalVariable(Token name) {
        return localVariables.findNode(name);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public int getMaxNumberArguments() {
        return parameters.size();
    }
}
