package lu.uni.serval.analytics;

import lu.uni.serval.utils.tree.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class CloneResults {
    private enum CloneType{
        None, Same, Synonym, Homonym
    }

    private Map<CloneType, Map<TreeNode, List<Pair<TreeNode, CloneIndex>>>> results;

    public CloneResults(){
        results = new HashMap<CloneType, Map<TreeNode, List<Pair<TreeNode, CloneIndex>>>>();

        results.put(CloneType.Same, new HashMap<TreeNode, List<Pair<TreeNode, CloneIndex>>>());
        results.put(CloneType.Synonym, new HashMap<TreeNode, List<Pair<TreeNode, CloneIndex>>>());
        results.put(CloneType.Homonym, new HashMap<TreeNode, List<Pair<TreeNode, CloneIndex>>>());
    }

    public Map<TreeNode, List<TreeNode>> getSame(){
        return simplify(results.get(CloneType.Same));
    }

    public Map<TreeNode, List<TreeNode>> getSynonym(){
        return simplify(results.get(CloneType.Synonym));
    }

    public Map<TreeNode, List<TreeNode>> getHomonym(){
        return simplify(results.get(CloneType.Homonym));
    }

    public void update(CloneIndex cloneIndex, TreeNode tree1, TreeNode tree2) {
        CloneType type = CloneType.None;

        if(cloneIndex.isSame()){
            type = CloneType.Same;
        } else if(cloneIndex.isHomonym()){
            type = CloneType.Homonym;
        } else if(cloneIndex.isSynonym()){
            type = CloneType.Synonym;
        }

        if(type != CloneType.None){
            update(results.get(type), cloneIndex, tree1, tree2);
            update(results.get(type), cloneIndex, tree2, tree1);
        }
    }

    private void update(Map<TreeNode, List<Pair<TreeNode, CloneIndex>>> clones, CloneIndex cloneIndex,
                        TreeNode tree1, TreeNode tree2){
        if(clones == null){
            return;
        }

        List<Pair<TreeNode, CloneIndex>> cloneList = clones.get(tree1);

        if(cloneList == null){
            cloneList = new ArrayList<Pair<TreeNode,CloneIndex>>();
            clones.put(tree1, cloneList);
        }

        Pair<TreeNode, CloneIndex> clone = new ImmutablePair<TreeNode, CloneIndex>(tree2, cloneIndex);

        if(!cloneList.contains(clone)){
            cloneList.add(clone);
        }
    }

    public Map<TreeNode, List<TreeNode>> simplify(Map<TreeNode, List<Pair<TreeNode, CloneIndex>>> complexClones){
        Map<TreeNode, List<TreeNode>> simpleClones = new HashMap<TreeNode, List<TreeNode>>();

        for(Map.Entry<TreeNode, List<Pair<TreeNode, CloneIndex>>> complexClone: complexClones.entrySet()){
            List<TreeNode> simpleClone = new ArrayList<TreeNode>();
            for (Pair<TreeNode, CloneIndex> pair : complexClone.getValue()){
                simpleClone.add(pair.getLeft());
            }

            simpleClones.put(complexClone.getKey(), simpleClone);
        }

        return simpleClones;
    }
}
