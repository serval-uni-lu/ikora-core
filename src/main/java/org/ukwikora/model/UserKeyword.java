package org.ukwikora.model;

import java.util.ArrayList;
import java.util.List;

public class UserKeyword extends KeywordDefinition {
    private List<String> parameters;
    private StatementTable<Variable> localVariables;

    public UserKeyword() {
        parameters = new ArrayList<>();
        localVariables = new StatementTable<>();
    }

    @Override
    public void setName(String name) {
        super.setName(name);

        for(String argument: getNameAsArgument().findVariables()){
            addParameter(argument);
        }
    }

    @Override
    public void addStep(Step step){
        super.addStep(step);

        if(Assignment.class.isAssignableFrom(step.getClass())){
            for (Variable variable: ((Assignment)step).getReturnValues()){
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

    public void addParameter(String parameter){
        parameters.add(parameter);

        Variable variable = new ScalarVariable();
        variable.setName(parameter);

        localVariables.add(variable);
    }

    public List<String> getParameters() {
        return parameters;
    }

    public Variable findLocalVariable(String name) {
        return localVariables.findStatement(name);
    }

    @Override
    public void accept(StatementVisitor visitor){
        visitor.visit(this);
    }
}
