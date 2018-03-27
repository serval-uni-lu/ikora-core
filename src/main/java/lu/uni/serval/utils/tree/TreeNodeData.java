package lu.uni.serval.utils.tree;

public interface TreeNodeData {
    String getLabel();
    boolean isSame(TreeNodeData other);
    boolean isValid();
}
