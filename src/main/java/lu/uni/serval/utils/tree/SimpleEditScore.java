package lu.uni.serval.utils.tree;

public class SimpleEditScore implements EditScore {
    public double replace(TreeNode node1, TreeNode node2) {
        if (node1.getLabel().equals(node2.getLabel())){
            return 0;
        }
        return 1;
    }

    public double delete(TreeNode node1, TreeNode node2) {
        if(node2 == null){
            return node1.getSize();
        }

        return 1;
    }

    public double insert(TreeNode node1, TreeNode node2) {
        if(node1 == null){
            return node2.getSize();
        }

        return 1;
    }

    public double size(TreeNode tree1, TreeNode tree2) {
        return tree1.getSize() > tree2.getSize() ? tree1.getSize() : tree2.getSize();
    }
}
