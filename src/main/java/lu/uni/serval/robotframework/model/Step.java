package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.tree.LabelTreeNode;
import lu.uni.serval.utils.tree.TreeNodeData;

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
    public String getLabel() {
        return getName().toString();
    }

    @Override
    public boolean isSame(TreeNodeData other) {
        return this == other;
    }

    @Override
    public boolean isValid() {
        return parent != null;
    }
}
