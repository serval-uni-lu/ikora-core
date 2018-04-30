package lu.uni.serval.analytics;

import lu.uni.serval.utils.KeywordData;
import lu.uni.serval.utils.tree.TreeNode;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;

public class KeywordSequence implements Iterable<List<TreeNode>>{
    private Map<ImmutablePair<String, String>, List<TreeNode>> data;

    public KeywordSequence(){
        data = new HashMap<>();
    }

    public void add(TreeNode keyword){
        String name = ((KeywordData)keyword.data).name;
        String library = ((KeywordData)keyword.data).library;

        ImmutablePair<String, String> key = new ImmutablePair<>(name, library);

        if(data.containsKey(key)){
            data.get(key).add(keyword);
        }
        else{
            List<TreeNode> keywords = new ArrayList<>();
            keywords.add(keyword);

            data.put(key, keywords);
        }
    }


    @Override
    public Iterator<List<TreeNode>> iterator() {
        return data.values().iterator();
    }
}
