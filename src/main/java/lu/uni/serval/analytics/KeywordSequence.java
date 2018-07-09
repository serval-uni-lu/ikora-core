package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.KeywordDefinition;
import lu.uni.serval.robotframework.model.Project;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;

public class KeywordSequence implements Iterable<List<ImmutablePair<Project, KeywordDefinition>>>{
    private Map<List<String>, List<ImmutablePair<Project, KeywordDefinition>>> data;

    public KeywordSequence(){
        data = new HashMap<>();
    }

    public void add(Project project, KeywordDefinition keyword){
        List<String> key = new ArrayList<>();
        key.add(keyword.getFile());
        key.add(keyword.getName().toString());

        if(data.containsKey(key)){
            data.get(key).add(ImmutablePair.of(project,keyword));
        }
        else{
            List<KeywordDefinition> keywords = new ArrayList<>();
            keywords.add(keyword);

            data.put(key, Collections.singletonList(new ImmutablePair(project, keywords)));
        }
    }

    @Override
    public Iterator<List<ImmutablePair<Project, KeywordDefinition>>> iterator() {
        return data.values().iterator();
    }
}
