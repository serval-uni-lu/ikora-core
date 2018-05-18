package lu.uni.serval.utils.tree;

public interface EditScore {
    double replace(LabelTreeNode node1, LabelTreeNode node2);
    double delete(LabelTreeNode node, LabelTreeNode node2);
    double insert(LabelTreeNode node, LabelTreeNode node2);
    double size(LabelTreeNode tree1, LabelTreeNode tree2);
}
