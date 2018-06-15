package lu.uni.serval.robotframework.model;

import java.util.ArrayList;
import java.util.List;

public class UserKeyword extends KeywordDefinition {
    protected List<Argument> arguments;
    private List<String> tags;

    public UserKeyword() {
        arguments = new ArrayList<>();
        tags = new ArrayList<>();
    }

    public void addArgument(String argument){
        arguments.add(new Argument(argument));
    }

    public void addTag(String tag){
        this.tags.add(tag);
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public List<String> getTags() {
        return tags;
    }
}
