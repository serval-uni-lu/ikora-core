package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.report.KeywordStatus;

import java.util.*;

public class KeywordSequence implements Iterable<List<KeywordStatus>>{
    private Map<List<String>, List<KeywordStatus>> data;

    public KeywordSequence(){
        data = new HashMap<>();
    }

    public void add(KeywordStatus keyword){
        List<String> key = new ArrayList<>();
        key.add(keyword.getSource());
        key.add(keyword.getName());

        if(data.containsKey(key)){
            data.get(key).add(keyword);
        }
        else{
            List<KeywordStatus> keywords = new ArrayList<>();
            keywords.add(keyword);

            data.put(key, keywords);
        }
    }

    @Override
    public Iterator<List<KeywordStatus>> iterator() {
        return data.values().iterator();
    }
}
