package lu.uni.serval.robotframework.model;

import lu.uni.serval.robotframework.runner.Runtime;

import lu.uni.serval.utils.tree.LabelTreeNode;

import java.util.Set;

public interface Keyword {
    Keyword getStep(int position);
    LabelTreeNode getNode();
    Set<Keyword> getDependencies();
    Argument getName();

    void addDependency(Keyword keyword);

    void execute(Runtime runtime);
}
