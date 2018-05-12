package lu.uni.serval.analytics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lu.uni.serval.utils.CompareCache;
import lu.uni.serval.utils.tree.LabelTreeNode;

import java.util.HashMap;
import java.util.Map;

@JsonSerialize(using = CloneResultSerializer.class)
public class CloneResults {
    public enum CloneType{
        None, Same, Synonym, Homonym
    }

    private Map<CloneType, CompareCache<LabelTreeNode, CloneIndex>> results;

    public CloneResults(){
        results = new HashMap<>();

        results.put(CloneType.Same, new CompareCache<>());
        results.put(CloneType.Synonym, new CompareCache<>());
        results.put(CloneType.Homonym, new CompareCache<>());
    }

    public CompareCache<LabelTreeNode, CloneIndex> getSame(){
        return results.get(CloneType.Same);
    }

    public CompareCache<LabelTreeNode, CloneIndex> getSynonym(){
        return results.get(CloneType.Synonym);
    }

    public CompareCache<LabelTreeNode, CloneIndex> getHomonym(){
        return results.get(CloneType.Homonym);
    }

    public CompareCache<LabelTreeNode, CloneIndex> getByType(CloneType type){
        return results.get(type);
    }

    public void update(CloneIndex cloneIndex, LabelTreeNode tree1, LabelTreeNode tree2) {
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
        }
    }

    private void update(CompareCache<LabelTreeNode, CloneIndex> clones, CloneIndex cloneIndex,
                        LabelTreeNode tree1, LabelTreeNode tree2){
        if(clones == null){
            return;
        }

        clones.set(tree1, tree2, cloneIndex);
    }
}
