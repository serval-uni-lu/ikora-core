package lu.uni.serval.robotframework.model;

import lu.uni.serval.robotframework.runner.Runtime;

import lu.uni.serval.utils.tree.LabelTreeNode;
import lu.uni.serval.utils.tree.TreeNodeData;

import java.util.Set;

public interface Keyword extends TreeNodeData {
    LabelTreeNode getNode();
    Set<Keyword> getDependencies();

    void addDependency(Keyword keyword);

    void execute(Runtime runtime);
}
