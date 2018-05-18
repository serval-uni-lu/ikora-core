package lu.uni.serval.analytics;

import lu.uni.serval.utils.exception.DuplicateNodeException;
import lu.uni.serval.utils.tree.LabelTreeNode;
import lu.uni.serval.utils.tree.TreeEditDistance;

import static lu.uni.serval.utils.nlp.StringUtils.levenshteinIndex;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class CloneDetection {
    private final TreeEditDistance treeEditDistance;

    public CloneDetection(){
        treeEditDistance = new TreeEditDistance(1.0, 1.0, 1.0);
    }

    public CloneResults findClones(final Set<LabelTreeNode> forest) throws DuplicateNodeException {
        CloneResults results = new CloneResults();
        List<LabelTreeNode> forestArray = new ArrayList<>(forest);

        Map<LabelTreeNode, LabelTreeNode> semanticMap = createSemanticMap(forestArray);

        for (int i = 0; i < forest.size(); ++i){
            for (int j = i + 1; j < forest.size(); ++j){
                CloneIndex cloneIndex = computeCloneIndex(forestArray.get(i), forestArray.get(j), semanticMap);
                results.update(cloneIndex, forestArray.get(i), forestArray.get(j));
            }
        }

        return results;
    }

    public CloneIndex computeCloneIndex(final LabelTreeNode tree1, final LabelTreeNode tree2) throws DuplicateNodeException {
        List<LabelTreeNode> forest = new ArrayList<>(2);
        forest.add(tree1);
        forest.add(tree2);

        Map<LabelTreeNode, LabelTreeNode> semanticMap = createSemanticMap(forest);

        return computeCloneIndex(tree1, tree2, semanticMap);
    }

    private Map<LabelTreeNode, LabelTreeNode> createSemanticMap(final List<LabelTreeNode> forest) throws DuplicateNodeException {

        Map<LabelTreeNode, LabelTreeNode> semanticMap = new HashMap<>();

        for(LabelTreeNode tree: forest){
            LabelTreeNode actionTree = new LabelTreeNode(tree.getData());

            for(LabelTreeNode leaf: tree.getLeaves()){
                actionTree.add(leaf.getData());
            }

            semanticMap.put(tree, actionTree);
        }

        return semanticMap;
    }

    private CloneIndex computeCloneIndex(final LabelTreeNode tree1, final LabelTreeNode tree2, final Map<LabelTreeNode, LabelTreeNode> sementicMap){
        double treeIndex;
        double keywordIndex;

        if(checkSubtree(tree1, tree2)) {
            treeIndex = CloneIndex.Ignore.Subtree.getValue();
            keywordIndex = CloneIndex.Ignore.Subtree.getValue();
        }
        else if(tree1.getChildCount() == 1 && tree2.getChildCount() == 1) {
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

    private boolean checkSubtree(final LabelTreeNode tree1, final LabelTreeNode tree2){
        return tree1.getDepth() > tree2.getDepth() ? isSubtree(tree1, tree2) : isSubtree(tree2, tree1);
    }

    private boolean isSubtree(LabelTreeNode tree1, LabelTreeNode tree2){
        if(tree1.getData().isSame(tree2.getData()) && tree1.getDepth() == tree2.getDepth()){
            return true;
        }

        if(tree2.getDepth() >= tree1.getDepth()){
            return false;
        }

        for(int i = 0; i < tree1.getChildCount(); ++i){
            if(isSubtree((LabelTreeNode)tree1.getChildAt(i), tree2)){
                return true;
            }
        }

        return false;
    }
}
