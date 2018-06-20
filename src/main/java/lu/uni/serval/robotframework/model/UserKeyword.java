package lu.uni.serval.robotframework.model;

import java.util.ArrayList;
import java.util.List;

public class UserKeyword extends KeywordDefinition {
    private List<String> parameters;
    private VariableTable localVariables;

    public UserKeyword() {
        parameters = new ArrayList<>();
        localVariables = new VariableTable();
    }

    @Override
    public void setName(String name) {
        super.setName(name);

        for(String argument: getName().findVariables()){
            addParameter(argument);
        }
    }

    public void addParameter(String parameter){
        parameters.add(parameter);

        Variable variable = new Variable();
        variable.setName(parameter);

        localVariables.put(variable.getName(), variable);
    }

    public List<String> getParameters() {
        return parameters;
    }

    public Variable findLocalVariable(String name) {
        return localVariables.get(name);
    }
}
