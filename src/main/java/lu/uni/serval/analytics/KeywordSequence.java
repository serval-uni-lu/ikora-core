package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.KeywordDefinition;
import lu.uni.serval.robotframework.model.Project;

import java.util.*;

public class KeywordSequence implements Iterable<List<KeywordInfo>>{
    private Map<List<String>, List<KeywordInfo>> data;

    public KeywordSequence(){
        data = new HashMap<>();
    }

    public void add(Project project, KeywordDefinition keyword){
        KeywordInfo keywordInfo = new KeywordInfo(project, keyword);

        List<String> key = new ArrayList<>();
        key.add(keyword.getFile());
        key.add(keyword.getName().toString());

        if(data.containsKey(key)){
            data.get(key).add(keywordInfo);
        }
        else{
            List<KeywordInfo> keywords = new ArrayList<>();
            keywords.add(keywordInfo);

            data.put(key, keywords);
        }
    }

    @Override
    public Iterator<List<KeywordInfo>> iterator() {
        return data.values().iterator();
    }
}
