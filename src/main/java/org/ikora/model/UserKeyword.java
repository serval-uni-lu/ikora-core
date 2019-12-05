package org.ikora.model;

import org.ikora.analytics.NodeVisitor;
import org.ikora.analytics.VisitorMemory;

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

    public void setReturn(String[] returnString) {
        returnValues.clear();

        for (String string : returnString) {
            returnValues.add(new Value(string));
        }
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
    public boolean isDeadCode(){
        return getDependencies().size() == 0;
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
    public Value.Type[] getArgumentTypes() {
        Value.Type[] types = new Value.Type[parameters.size()];

        for(int i = 0; i < types.length; ++i){
            types[i] = Value.Type.String;
        }

        return types;
    }

    @Override
    public int getMaxArgument(){
        return parameters.size();
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
}
