package lu.uni.serval.robotframework.model;

import java.util.ArrayList;
import java.util.List;

public class UserKeyword extends KeywordDefinition {
    private List<String> parameters;
    private ElementTable<Variable> localVariables;

    public UserKeyword() {
        parameters = new ArrayList<>();
        localVariables = new ElementTable<>();
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

        localVariables.add(variable);
    }

    public List<String> getParameters() {
        return parameters;
    }

    public Variable findLocalVariable(String name) {
        return localVariables.findElement(name);
    }
}
