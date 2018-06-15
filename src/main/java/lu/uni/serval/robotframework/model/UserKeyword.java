package lu.uni.serval.robotframework.model;

import java.util.ArrayList;
import java.util.List;

public class UserKeyword extends KeywordDefinition {
    protected List<String> arguments;
    private List<String> tags;

    public UserKeyword() {
        arguments = new ArrayList<>();
        tags = new ArrayList<>();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        arguments.addAll(Argument.findVariables(name));
    }

    public void addArgument(String argument){
        arguments.add(argument);
    }

    public void addTag(String tag){
        this.tags.add(tag);
    }

    public List<String> getArguments() {
        return arguments;
    }

    public List<String> getTags() {
        return tags;
    }
}
