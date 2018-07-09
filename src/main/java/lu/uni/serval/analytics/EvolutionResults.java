package lu.uni.serval.analytics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lu.uni.serval.robotframework.model.Project;
import lu.uni.serval.utils.UnorderedPair;

import java.util.*;

@JsonSerialize(using = EvolutionResultsSerializer.class)
public class EvolutionResults implements Map<UnorderedPair<Project>, Difference>{
    private Map<UnorderedPair<Project>, Difference> differences;

    EvolutionResults(){
        differences = new LinkedHashMap<>();
    }

    public void addDifference(Project left, Project right, Difference difference) {
        put(UnorderedPair.of(left, right), difference);
    }

    @Override
    public int size() {
        return differences.size();
    }

    @Override
    public boolean isEmpty() {
        return differences.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return differences.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return differences.containsValue(value);
    }

    @Override
    public Difference get(Object key) {
        return differences.get(key);
    }

    @Override
    public Difference put(UnorderedPair<Project> key, Difference value) {
        return differences.put(key, value);
    }

    @Override
    public Difference remove(Object key) {
        return differences.remove(key);
    }

    @Override
    public void putAll(Map<? extends UnorderedPair<Project>, ? extends Difference> m) {
        differences.putAll(m);
    }

    @Override
    public void clear() {
        differences.clear();
    }

    @Override
    public Set<UnorderedPair<Project>> keySet() {
        return differences.keySet();
    }

    @Override
    public Collection<Difference> values() {
        return differences.values();
    }

    @Override
    public Set<Entry<UnorderedPair<Project>, Difference>> entrySet() {
        return differences.entrySet();
    }
}
