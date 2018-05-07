package lu.uni.serval.analytics;

import lu.uni.serval.utils.KeywordData;
import lu.uni.serval.utils.tree.TreeNode;

import java.util.*;



public class KeywordSequence implements Iterable<List<TreeNode>>{
    private Map<List<String>, List<TreeNode>> data;

    public KeywordSequence(){
        data = new HashMap<>();
    }

    public void add(TreeNode keyword){
        List<String> key = new ArrayList<>();
        key.add(((KeywordData)keyword.data).file);
        key.add(((KeywordData)keyword.data).name);
        key.add(((KeywordData)keyword.data).library);

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
