package lu.uni.serval.utils.tree;

public interface EditScore {
    double replace(TreeNode node1, TreeNode node2);
    double delete(TreeNode node);
    double insert(TreeNode node);
    double size(TreeNode tree1, TreeNode tree2);
}
