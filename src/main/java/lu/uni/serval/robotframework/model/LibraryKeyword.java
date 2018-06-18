package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.tree.LabelTreeNode;
import lu.uni.serval.utils.tree.TreeNodeData;

import java.util.HashSet;
import java.util.Set;


public abstract class LibraryKeyword implements Keyword {
    private Set<Keyword> dependencies;
    private LabelTreeNode node;

    public LibraryKeyword() {
        this.dependencies = new HashSet<>();
        this.node = new LabelTreeNode(this);
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public boolean isSame(TreeNodeData other) {
        return false;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public LabelTreeNode getNode() {
        return node;
    }

    @Override
    public Set<Keyword> getDependencies() {
        return dependencies;
    }

    @Override
    public void addDependency(Keyword keyword) {
        this.dependencies.add(keyword);
    }

    public abstract void execute();

}
