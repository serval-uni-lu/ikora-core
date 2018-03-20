package lu.uni.serval.utils.tree;

import lu.uni.serval.utils.CompareCache;

public class TreeEditDistance implements TreeDistance {
    private final EditScore score;

    public TreeEditDistance(EditScore score) {
        if(score == null) {
            throw new NullPointerException();
        }

        this.score = score;
    }

    public double distance(TreeNode tree1, TreeNode tree2) {
        if (tree1 == null || tree2 == null) {
            throw new NullPointerException();
        }

        CompareCache<TreeNode, ScoreElement> memory = new CompareCache<TreeNode, ScoreElement>();
        return distance(memory, tree1, tree2);
    }

    public double index(TreeNode tree1, TreeNode tree2) {
        if(tree1.getSize() == 0 && tree2.getSize() == 0) {
            return 0;
        }

        double distance = distance(tree1, tree2);
        double size = score.size(tree1, tree2);

        return distance / size;
    }

    private double distance(CompareCache<TreeNode, ScoreElement> memory, TreeNode tree1, TreeNode tree2) {
        if(memory.isCached(tree1, tree2)) {
            return memory.getScore(tree1, tree2).score;
        }

        double score;
        EditOperation operation;

        if (tree1 == null && tree2 == null) {
            return 0.0;
        }
        else if (tree1 == null) {
            score = calculateInsertScore(memory, tree1, tree2);
            operation = EditOperation.Insert;
        }
        else if (tree2 == null) {
            score = calculateDeleteScore(memory, tree1, tree2);
            operation = EditOperation.Delete;
        }
        else {
            double replace = calculateReplaceScore(memory, tree1, tree2);
            double delete = calculateDeleteScore(memory, tree1, tree2);
            double insert = calculateInsertScore(memory, tree1, tree2);

            if(replace < delete && replace < insert) {
                score = replace;
                operation = EditOperation.Replace;
            }
            else if (delete < insert) {
                score = delete;
                operation = EditOperation.Delete;
            }
            else {
                score = insert;
                operation = EditOperation.Insert;
            }
        }

        memory.set(tree1, tree2, new ScoreElement(score, operation));
        return score;
    }

    private double calculateReplaceScore(CompareCache<TreeNode, ScoreElement> memory, TreeNode tree1, TreeNode tree2) {
        double s1 = distance(memory, getInside(tree1), getInside(tree2));
        double s2 = distance(memory, getOutside(tree1), getOutside(tree2));
        return s1 + s2 + score.replace(tree1, tree2);
    }

    private double calculateInsertScore(CompareCache<TreeNode, ScoreElement> memory, TreeNode tree1, TreeNode tree2) {
        TreeNode newTree = deleteHead(tree2);
        return distance(memory, tree1, newTree) + score.insert(newTree);
    }

    private double calculateDeleteScore(CompareCache<TreeNode, ScoreElement> memory, TreeNode tree1, TreeNode tree2) {
        TreeNode newTree = deleteHead(tree1);
        return distance(memory, newTree, tree2) + score.delete(newTree);
    }

    private TreeNode getInside(TreeNode node) {
        return node.getFirstChild();
    }

    private TreeNode getOutside(TreeNode node) {
        while(!node.isRoot()){
            TreeNode sibling = node.getNextSibling();

            if(sibling != null) {
                return sibling;
            }

            node = node.getParent();
        }

        return null;
    }

    private TreeNode deleteHead(TreeNode node) {
         TreeNode child = node.getFirstChild();

         if(child == null){
             return getOutside(node);
         }

         return child;
    }
}
