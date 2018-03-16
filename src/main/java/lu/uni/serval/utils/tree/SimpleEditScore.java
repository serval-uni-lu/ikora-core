package lu.uni.serval.utils.tree;

public class SimpleEditScore implements EditScore {
    public double replace(TreeNode node1, TreeNode node2) {
        if (node1.getLabel().equals(node2.getLabel())){
            return 0;
        }
        return 1;
    }

    public double delete(TreeNode node) {
        return 1;
    }

    public double insert(TreeNode node) {
        return 1;
    }

    public double size(TreeNode tree1, TreeNode tree2) {
        return tree1.getSize() > tree2.getSize() ? tree1.getSize() : tree2.getSize();
    }
}
