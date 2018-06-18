package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.tree.TreeNodeData;


public abstract class LibraryKeyword implements TreeNodeData {
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

    public abstract void execute();
}
