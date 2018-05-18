package lu.uni.serval.utils.tree;

import static lu.uni.serval.utils.nlp.StringUtils.levenshteinIndex;

public class LevenshteinEditScore implements EditScore {
    public double replace(LabelTreeNode node1, LabelTreeNode node2) {
        String label1 = node1.getLabel();
        String label2 = node2.getLabel();

        if(label1.equals(label2)) {
            return 0;
        }

        return levenshteinIndex(label1,label2);
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
