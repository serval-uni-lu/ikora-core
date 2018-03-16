package lu.uni.serval.analytics;

import lu.uni.serval.utils.tree.CloneEditScore;
import lu.uni.serval.utils.tree.TreeEditDistance;
import lu.uni.serval.utils.tree.TreeNode;

import static lu.uni.serval.utils.nlp.StringUtils.levenshteinIndex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloneDetection {
    private final TreeEditDistance treeEditDistance;

    public CloneDetection(){
        treeEditDistance = new TreeEditDistance(new CloneEditScore());
    }

    public Map<TreeNode, TreeNode> findClones(final List<TreeNode> forest){
        Map<TreeNode, TreeNode> clones = new HashMap<TreeNode, TreeNode>();

        for (int i = 0; i < forest.size(); ++i){
            for (int j = i; j < forest.size(); ++j){
                CloneIndex cloneIndex = computeCloneIndex(forest.get(i), forest.get(j));
            }
        }

        return clones;
    }

    public CloneIndex computeCloneIndex(final TreeNode tree1, final TreeNode tree2){
        double treeIndex = treeEditDistance.index(tree1, tree2);
        double keywordIndex = levenshteinIndex(tree1.getLabel(), tree2.getLabel());

        return new CloneIndex(keywordIndex, treeIndex);
    }
}
