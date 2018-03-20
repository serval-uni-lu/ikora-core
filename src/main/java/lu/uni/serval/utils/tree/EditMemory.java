package lu.uni.serval.utils.tree;

import lu.uni.serval.utils.UnorderedPair;

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

    private HashMap<UnorderedPair<TreeNode>, CacheElement> map;

    public EditMemory(){
        this.map = new HashMap<UnorderedPair<TreeNode>, CacheElement>();
    }

    public boolean cached(TreeNode tree1, TreeNode tree2) {
        return map.containsKey(new UnorderedPair<TreeNode>(tree1, tree2));
    }

    public double getScore(TreeNode tree1, TreeNode tree2) {
        return map.get(new UnorderedPair<TreeNode>(tree1, tree2)).score;
    }

    public EditOperation getOperation(TreeNode tree1, TreeNode tree2) {
        return map.get(new UnorderedPair<TreeNode>(tree1, tree2)).operation;
    }

    public void set(TreeNode tree1, TreeNode tree2, double score, EditOperation operation) {
        UnorderedPair<TreeNode> pair = new UnorderedPair<TreeNode>(tree1, tree2);
        CacheElement cache = new CacheElement(score, operation);

        map.put(pair, cache);
    }
}
