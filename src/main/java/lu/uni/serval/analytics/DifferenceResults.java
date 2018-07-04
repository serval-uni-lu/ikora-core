package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.Keyword;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class DifferenceResults implements Map<Pair<Keyword, Keyword>, Difference>{
    private Map<Pair<Keyword, Keyword>, Difference> differences;

    DifferenceResults(){
        differences = new LinkedHashMap<>();
    }

    public void addDifference(Difference difference) {
        Pair<Keyword, Keyword> dates = new ImmutablePair<>(difference.getBefore(), difference.getAfter());
        put(dates, difference);
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
    public Difference put(Pair<Keyword, Keyword> key, Difference value) {
        return differences.put(key, value);
    }

    @Override
    public Difference remove(Object key) {
        return differences.remove(key);
    }

    @Override
    public void putAll(Map<? extends Pair<Keyword, Keyword>, ? extends Difference> m) {
        differences.putAll(m);
    }

    @Override
    public void clear() {
        differences.clear();
    }

    @Override
    public Set<Pair<Keyword, Keyword>> keySet() {
        return differences.keySet();
    }

    @Override
    public Collection<Difference> values() {
        return differences.values();
    }

    @Override
    public Set<Entry<Pair<Keyword, Keyword>, Difference>> entrySet() {
        return differences.entrySet();
    }
}
