package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.tree.LabelTreeNode;

import java.util.*;


public abstract class Step implements Keyword {
    private Argument name;
    private Keyword parent;

    public Step() {

    }

    public void setName(String name) {
        this.name = new Argument(name);
    }

    public void setParent(KeywordDefinition parent) {
        this.parent = parent;
        this.parent.addDependency(this);
    }

    public Argument getName() {
        return this.name;
    }

    public Keyword getParent() {
        return parent;
    }

    public abstract List<Argument> getParameters();

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

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Step)) {
            return false;
        }

        Step step = (Step)other;

        boolean same = name.equals(step.name);

        return same;
    }
}
