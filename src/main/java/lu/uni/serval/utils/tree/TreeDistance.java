package lu.uni.serval.utils.tree;

public interface TreeDistance {
    double distance(LabelTreeNode tree1, LabelTreeNode tree2);
    double index(LabelTreeNode tree1, LabelTreeNode tree2);
}
