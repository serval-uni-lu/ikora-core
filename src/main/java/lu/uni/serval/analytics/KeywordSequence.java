package lu.uni.serval.analytics;

import lu.uni.serval.utils.KeywordData;
import lu.uni.serval.utils.tree.LabelTreeNode;

import java.util.*;



public class KeywordSequence implements Iterable<List<LabelTreeNode>>{
    private Map<List<String>, List<LabelTreeNode>> data;

    public KeywordSequence(){
        data = new HashMap<>();
    }

    public void add(LabelTreeNode keyword){
        List<String> key = new ArrayList<>();
        key.add(((KeywordData)keyword.getData()).file);
        key.add(((KeywordData)keyword.getData()).name);
        key.add(((KeywordData)keyword.getData()).library);

        if(data.containsKey(key)){
            data.get(key).add(keyword);
        }
        else{
            List<LabelTreeNode> keywords = new ArrayList<>();
            keywords.add(keyword);

            data.put(key, keywords);
        }
    }

    @Override
    public Iterator<List<LabelTreeNode>> iterator() {
        return data.values().iterator();
    }
}
