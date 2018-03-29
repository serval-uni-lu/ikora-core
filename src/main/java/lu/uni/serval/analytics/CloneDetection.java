package lu.uni.serval.analytics;

import lu.uni.serval.utils.exception.DuplicateNodeException;
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

    public CloneResults findClones(final List<TreeNode> forest) throws DuplicateNodeException {
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

    public CloneIndex computeCloneIndex(final TreeNode tree1, final TreeNode tree2) throws DuplicateNodeException {
        List<TreeNode> forest = new ArrayList<>(2);
        forest.add(tree1);
        forest.add(tree2);

        Map<TreeNode, TreeNode> semanticMap = createSemanticMap(forest);

        return computeCloneIndex(tree1, tree2, semanticMap);
    }

    private Map<TreeNode, TreeNode> createSemanticMap(final List<TreeNode> forest) throws DuplicateNodeException {

        Map<TreeNode, TreeNode> semanticMap = new HashMap<>();

        for(TreeNode tree: forest){
            TreeNode actionTree = new TreeNode(tree.data, true);

            for(TreeNode leaf: tree.getLeaves()){
                actionTree.addChild(leaf.data);
            }

            semanticMap.put(tree, actionTree);
        }

        return semanticMap;
    }

    private CloneIndex computeCloneIndex(final TreeNode tree1, final TreeNode tree2, final Map<TreeNode, TreeNode> sementicMap){
        double treeIndex;
        double keywordIndex;

        if(checkSubtree(tree1, tree2)) {
            treeIndex = CloneIndex.Ignore.Subtree.getValue();
            keywordIndex = CloneIndex.Ignore.Subtree.getValue();
        }
        else if(tree1.children.size() == 1 && tree2.children.size() == 1) {
            treeIndex = CloneIndex.Ignore.OneStep.getValue();
            keywordIndex = CloneIndex.Ignore.OneStep.getValue();
        }
        else{
            treeIndex = 1- treeEditDistance.index(tree1, tree2);
            keywordIndex = 1- levenshteinIndex(tree1.getLabel(), tree2.getLabel());
        }

        double semanticIndex = 1 - treeEditDistance.index(sementicMap.get(tree1), sementicMap.get(tree2));

        return new CloneIndex(keywordIndex, treeIndex, semanticIndex);
    }

    private boolean checkSubtree(final TreeNode tree1, final TreeNode tree2){
        return tree1.getDepth() > tree2.getDepth() ? isSubtree(tree1, tree2) : isSubtree(tree2, tree1);
    }

    private boolean isSubtree(TreeNode tree1, TreeNode tree2){
        if(!tree1.isDataUnique){
            return false;
        }

        if(tree1.data.isSame(tree2.data) && tree1.getDepth() == tree2.getDepth()){
            return true;
        }

        if(tree2.getDepth() >= tree1.getDepth()){
            return false;
        }

        for(TreeNode child: tree1.children){
            if(isSubtree(child, tree2)){
                return true;
            }
        }

        return false;
    }
}
