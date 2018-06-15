package lu.uni.serval.robotframework.model;

import java.util.*;

public class Step {
    private Argument name;
    private List<Argument> arguments;
    private KeywordDefinition parent;
    private KeywordDefinition keyword;

    public Step() {
        this.arguments = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = new Argument(name);
    }

    public void addArgument(String argument) {
        this.arguments.add(new Argument(argument));
    }

    public void setParent(KeywordDefinition parent) {
        this.parent = parent;
    }

    public void setKeyword(KeywordDefinition keyword){
        this.keyword = keyword;
        this.keyword.addDependency(parent);
        this.parent.node.add(this.keyword);
    }

    public Argument getName() {
        return this.name;
    }

    public List<Argument> getArguments() {
        return this.arguments;
    }

    public KeywordDefinition getKeyword() {
        return keyword;
    }

    public KeywordDefinition getParent() {
        return parent;
    }
}
