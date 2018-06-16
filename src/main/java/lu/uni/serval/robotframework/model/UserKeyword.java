package lu.uni.serval.robotframework.model;

import java.util.ArrayList;
import java.util.List;

public class UserKeyword extends KeywordDefinition {
    protected List<String> arguments;

    public UserKeyword() {
        arguments = new ArrayList<>();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        arguments.addAll(getName().findVariables());
    }

    public void addArgument(String argument){
        arguments.add(argument);
    }

    public List<String> getArguments() {
        return arguments;
    }
}
