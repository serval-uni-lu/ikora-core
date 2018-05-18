package lu.uni.serval.utils.tree;

public class SimpleEditScore implements EditScore {
    public double replace(LabelTreeNode node1, LabelTreeNode node2) {
        if (node1.getLabel().equals(node2.getLabel())){
            return 0;
        }
        return 1;
    }

    public double delete(LabelTreeNode node1, LabelTreeNode node2) {
        if(node2 == null){
            return node1.getNodeCount();
        }

        return 1;
    }

    public double insert(LabelTreeNode node1, LabelTreeNode node2) {
        if(node1 == null){
            return node2.getNodeCount();
        }

        return 1;
    }

    public double size(LabelTreeNode tree1, LabelTreeNode tree2) {
        return tree1.getNodeCount() > tree2.getNodeCount() ? tree1.getNodeCount() : tree2.getNodeCount();
    }
}
