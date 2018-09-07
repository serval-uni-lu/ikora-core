package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.KeywordDefinition;
import lu.uni.serval.robotframework.model.Project;

import java.util.*;

public class KeywordSequence implements Iterable<List<ElementInfo<KeywordDefinition>>>{
    private Map<List<String>, List<ElementInfo<KeywordDefinition>>> data;

    public KeywordSequence(){
        data = new HashMap<>();
    }

    public void add(Project project, KeywordDefinition keyword){
        ElementInfo<KeywordDefinition> keywordInfo = new ElementInfo<>(project, keyword);

        List<String> key = new ArrayList<>();
        key.add(keyword.getFile());
        key.add(keyword.getName().toString());

        if(data.containsKey(key)){
            data.get(key).add(keywordInfo);
        }
        else{
            List<ElementInfo<KeywordDefinition>> keywords = new ArrayList<>();
            keywords.add(keywordInfo);

            data.put(key, keywords);
        }
    }

    @Override
    public Iterator<List<ElementInfo<KeywordDefinition>>> iterator() {
        return data.values().iterator();
    }
}
