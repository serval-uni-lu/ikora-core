package lu.uni.serval.utils.tree;

import java.util.HashMap;

public class EditMemory {
    private static class CacheElement{
        public final double score;
        public final EditOperation operation;

        public CacheElement(double score, EditOperation operation){
            this.score = score;
            this.operation = operation;
        }
    }

    private class TreePair {
        public final TreeNode tree1;
        public final TreeNode tree2;

        public TreePair(TreeNode tree1, TreeNode tree2){
            this.tree1 = tree1;
            this.tree2 = tree2;
        }
    }

    private HashMap<TreePair, CacheElement> map;

    public EditMemory(){
        this.map = new HashMap<TreePair, CacheElement>();
    }

    public boolean cached(TreeNode tree1, TreeNode tree2) {
        return map.containsKey(new TreePair(tree1, tree2));
    }

    public double getScore(TreeNode tree1, TreeNode tree2) {
        return map.get(new TreePair(tree1, tree2)).score;
    }

    public EditOperation getOperation(TreeNode tree1, TreeNode tree2) {
        return map.get(new TreePair(tree1, tree2)).operation;
    }

    public void set(TreeNode tree1, TreeNode tree2, double score, EditOperation operation) {
        TreePair pair = new TreePair(tree1, tree2);
        CacheElement cache = new CacheElement(score, operation);

        map.put(pair, cache);
    }
}
