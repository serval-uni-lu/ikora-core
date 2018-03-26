package lu.uni.serval.analytics;

import lu.uni.serval.utils.tree.CloneEditScore;
import lu.uni.serval.utils.tree.TreeEditDistance;
import lu.uni.serval.utils.tree.TreeNode;

import static lu.uni.serval.utils.nlp.StringUtils.levenshteinIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloneDetection {
    private final TreeEditDistance treeEditDistance;

    public CloneDetection(){
        treeEditDistance = new TreeEditDistance(new CloneEditScore());
    }

    public CloneResults findClones(final List<TreeNode> forest){
        CloneResults results = new CloneResults();
        Map<TreeNode, TreeNode> semanticMap = createSemanticMap(forest);

        for (int i = 0; i < forest.size(); ++i){
            for (int j = i + 1; j < forest.size(); ++j){
                CloneIndex cloneIndex = computeCloneIndex(forest.get(i), forest.get(j), semanticMap);
                results.update(cloneIndex, forest.get(i), forest.get(j));
            }
        }

        return results;
    }

    public CloneIndex computeCloneIndex(final TreeNode tree1, final TreeNode tree2){
        List<TreeNode> forest = new ArrayList<TreeNode>(2);
        forest.add(tree1);
        forest.add(tree2);

        Map<TreeNode, TreeNode> semanticMap = createSemanticMap(forest);

        return computeCloneIndex(tree1, tree2, semanticMap);
    }

    private Map<TreeNode, TreeNode> createSemanticMap(final List<TreeNode> forest){

        Map<TreeNode, TreeNode> semanticMap = new HashMap<TreeNode, TreeNode>();

        for(TreeNode tree: forest){
            TreeNode actionTree = new TreeNode(tree.data);

            for(TreeNode leaf: tree.getLeaves()){
                actionTree.addChild(leaf.data);
            }

            semanticMap.put(tree, actionTree);
        }

        return semanticMap;
    }

    private CloneIndex computeCloneIndex(final  TreeNode tree1, final TreeNode tree2, final Map<TreeNode, TreeNode> sementicMap){
        double treeIndex = 1- treeEditDistance.index(tree1, tree2);
        double keywordIndex = 1- levenshteinIndex(tree1.getLabel(), tree2.getLabel());
        double semanticIndex = 1 - treeEditDistance.index(sementicMap.get(tree1), sementicMap.get(tree2));

        return new CloneIndex(keywordIndex, treeIndex, semanticIndex);
    }
}
