package lu.uni.serval.analytics;

import lu.uni.serval.utils.tree.CloneEditScore;
import lu.uni.serval.utils.tree.TreeEditDistance;
import lu.uni.serval.utils.tree.TreeNode;

public class CloneDetection {
    public static double calculateRatio(TreeNode tree1, TreeNode tree2){
        TreeEditDistance treeEditDistance = new TreeEditDistance(new CloneEditScore());
        return treeEditDistance.index(tree1, tree2);
    }
}
