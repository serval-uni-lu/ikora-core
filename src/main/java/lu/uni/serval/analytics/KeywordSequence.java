package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.KeywordDefinition;

import java.util.*;

public class KeywordSequence implements Iterable<List<KeywordDefinition>>{
    private Map<List<String>, List<KeywordDefinition>> data;

    public KeywordSequence(){
        data = new HashMap<>();
    }

    public void add(KeywordDefinition keyword){
        List<String> key = new ArrayList<>();
        key.add(keyword.getFile());
        key.add(keyword.getName().toString());

        if(data.containsKey(key)){
            data.get(key).add(keyword);
        }
        else{
            List<KeywordDefinition> keywords = new ArrayList<>();
            keywords.add(keyword);

            data.put(key, keywords);
        }
    }

    @Override
    public Iterator<List<KeywordDefinition>> iterator() {
        return data.values().iterator();
    }
}
