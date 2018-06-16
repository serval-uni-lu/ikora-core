package lu.uni.serval.robotframework.model;

import java.util.ArrayList;
import java.util.List;

public class UserKeyword extends KeywordDefinition {
    private List<String> arguments;
    private VariableTable localVariables;

    public UserKeyword() {
        arguments = new ArrayList<>();
        localVariables = new VariableTable();
    }

    @Override
    public void setName(String name) {
        super.setName(name);

        for(String argument: getName().findVariables()){
            addArgument(argument);
        }
    }

    public void addArgument(String argument){
        arguments.add(argument);

        Variable variable = new Variable();
        variable.setName(argument);

        localVariables.put(variable.getName(), variable);
    }

    public List<String> getArguments() {
        return arguments;
    }

    public Variable findLocalVariable(String name) {
        return localVariables.get(name);
    }
}
