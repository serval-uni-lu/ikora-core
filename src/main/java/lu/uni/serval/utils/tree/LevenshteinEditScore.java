package lu.uni.serval.utils.tree;

import static lu.uni.serval.utils.nlp.StringUtils.levenshteinIndex;

public class LevenshteinEditScore implements EditScore {
    public double replace(TreeNode node1, TreeNode node2) {
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
}
