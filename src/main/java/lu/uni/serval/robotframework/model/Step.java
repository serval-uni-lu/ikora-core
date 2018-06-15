package lu.uni.serval.robotframework.model;

import java.util.*;

public class Step implements KeywordCall {
    private String file;
    private Argument name;
    private List<Argument> arguments;
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

    public void setKeyword(KeywordDefinition keyword){
        this.keyword = keyword;
    }

    public String getFile() {
        return this.file;
    }

    public Argument getName() {
        return this.name;
    }

    public List<Argument> getArguments() {
        return this.arguments;
    }

    public boolean isKeywordLinked(){
        return keyword != null;
    }

}
