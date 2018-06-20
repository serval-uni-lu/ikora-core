package lu.uni.serval.robotframework.model;

import java.util.*;

public class Step {
    public enum Type{
        Keyword, Assignment, ForLoop
    }

    private Argument name;
    private List<Argument> parameter;
    private Keyword parent;
    private Keyword keyword;
    private Type type;

    public Step() {
        this.parameter = new ArrayList<>();
        keyword = null;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = new Argument(name);
    }

    public void addParameter(String argument) {
        this.parameter.add(new Argument(argument));
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

    public Type getType() {
        return type;
    }

    public Argument getName() {
        return this.name;
    }

    public List<Argument> getParameter() {
        return this.parameter;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public Keyword getParent() {
        return parent;
    }

    public boolean isSame(Step step) {
        if(step == null){
            return false;
        }

        if(parameter.size() != step.parameter.size()){
            return false;
        }

        boolean same = name.toString().equalsIgnoreCase(step.name.toString());

        for(int i = 0; same && i < parameter.size(); ++i){
            same &= parameter.get(i).toString().equalsIgnoreCase(step.parameter.get(i).toString());
        }

        return same;
    }
}
