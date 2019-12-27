package org.ikora.model;

import org.ikora.analytics.visitor.NodeVisitor;
import org.ikora.analytics.visitor.VisitorMemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserKeyword extends KeywordDefinition {
    private List<String> parameters;
    private NodeTable<Variable> localVariables;
    private KeywordCall tearDown;
    private List<Value> returnValues;
    private TimeOut timeOut;

    public UserKeyword() {
        parameters = new ArrayList<>();
        localVariables = new NodeTable<>();
        returnValues = new ArrayList<>();
        timeOut = null;
    }

    public KeywordCall getTearDown() {
        return tearDown;
    }

    public void setTearDown(KeywordCall tearDown) {
        this.tearDown = tearDown;
    }

    public void setTearDown(Step tearDown){
        setTearDown(toCall(tearDown));
    }

    public List<Value> getReturn(){
        return returnValues;
    }

    public void addReturn(String returnString) {
        returnValues.add(new Value(returnString));
    }

    public TimeOut getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(TimeOut timeOut){
        this.timeOut = timeOut;

        if(this.timeOut != null){

        }
    }

    @Override
    public void setName(String name) {
        super.setName(name);

        for(String argument: getNameAsValue().findVariables()){
            addParameter(argument);
        }
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
    public List<Value> getReturnValues() {
        return returnValues;
    }

    public void addParameter(String parameter){
        parameters.add(parameter);

        Variable variable = new ScalarVariable(parameter);
        localVariables.add(variable);
    }

    public List<String> getParameters() {
        return parameters;
    }

    public Set<Variable> findLocalVariable(String name) {
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