package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.tree.LabelTreeNode;

import java.util.*;


public abstract class Step implements Keyword {
    private Argument name;
    private List<Argument> parameter;
    private Keyword parent;

    public Step() {
        this.parameter = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = new Argument(name);
    }

    public void addParameter(String argument) {
        this.parameter.add(new Argument(argument));
    }

    public void setParent(KeywordDefinition parent) {
        this.parent = parent;
        this.parent.addDependency(this);
    }

    public Argument getName() {
        return this.name;
    }

    public List<Argument> getParameter() {
        return this.parameter;
    }

    public Keyword getParent() {
        return parent;
    }

    @Override
    public LabelTreeNode getNode() {
        return null;
    }

    @Override
    public Set<Keyword> getDependencies() {
        Set<Keyword> dependencies = new HashSet<>();
        dependencies.add(this.parent);

        return dependencies;
    }

    @Override
    public void addDependency(Keyword keyword) {
        this.parent = keyword;
    }
}
