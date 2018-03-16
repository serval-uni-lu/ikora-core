package lu.uni.serval.utils.tree;

import static lu.uni.serval.utils.nlp.StringUtils.levenshteinIndex;

public class CloneEditScore implements EditScore {

    public double replace(TreeNode node1, TreeNode node2) {
        if(node1.isRoot() && node2.isRoot()) {
            return 0;
        }

        String label1 = node1.getLabel();
        String label2 = node2.getLabel();

        if(label1.equals(label2)) {
            return 0;
        }

        return levenshteinIndex(label1,label2);
    }

    public double delete(TreeNode node) {
        return 1;
    }

    public double insert(TreeNode node) {
        return 1;
    }

    public double size(TreeNode tree1, TreeNode tree2) {
        return tree1.getSize() > tree2.getSize() ? tree1.getSize() - 1 : tree2.getSize() - 1;
    }
}
