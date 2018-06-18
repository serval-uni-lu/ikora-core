package lu.uni.serval.robotframework.model;

import java.util.*;

public class Step {
    private Argument name;
    private List<Argument> arguments;
    private Keyword parent;
    private Keyword keyword;

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

    public void setKeyword(Keyword keyword){
        this.keyword = keyword;

        if(this.keyword != null) {
            this.keyword.addDependency(parent);
            this.parent.getNode().add(this.keyword);
        }
    }

    public Argument getName() {
        return this.name;
    }

    public List<Argument> getArguments() {
        return this.arguments;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public Keyword getParent() {
        return parent;
    }
}
