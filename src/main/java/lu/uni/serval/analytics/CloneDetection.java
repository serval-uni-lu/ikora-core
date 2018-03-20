package lu.uni.serval.analytics;

import lu.uni.serval.utils.tree.CloneEditScore;
import lu.uni.serval.utils.tree.TreeEditDistance;
import lu.uni.serval.utils.tree.TreeNode;

import static lu.uni.serval.utils.nlp.StringUtils.levenshteinIndex;

import java.util.List;

public class CloneDetection {
    private final TreeEditDistance treeEditDistance;

    public CloneDetection(){
        treeEditDistance = new TreeEditDistance(new CloneEditScore());
    }

    public CloneResults findClones(final List<TreeNode> forest){
        CloneResults results = new CloneResults();

        for (int i = 0; i < forest.size(); ++i){
            for (int j = i + 1; j < forest.size(); ++j){
                System.out.println("Compare: " + forest.get(i).getLabel() + " -- " + forest.get(j).getLabel());
                CloneIndex cloneIndex = computeCloneIndex(forest.get(i), forest.get(j));
                results.update(cloneIndex, forest.get(i), forest.get(j));
            }
        }

        return results;
    }

    public CloneIndex computeCloneIndex(final TreeNode tree1, final TreeNode tree2){
        double treeIndex = 1- treeEditDistance.index(tree1, tree2);
        double keywordIndex = 1- levenshteinIndex(tree1.getLabel(), tree2.getLabel());

        return new CloneIndex(keywordIndex, treeIndex);
    }
}
