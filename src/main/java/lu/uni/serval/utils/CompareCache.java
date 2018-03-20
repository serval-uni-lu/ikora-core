package lu.uni.serval.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CompareCache<K, T> implements Iterable<Map.Entry<UnorderedPair<K>, T>> {
    private Map<UnorderedPair<K>, T> map;

    public CompareCache(){
        this.map = new HashMap<UnorderedPair<K>, T>();
    }

    public int size(){
        return this.map.size();
    }

    public boolean isCached(K key1, K key2){
        return map.containsKey(new UnorderedPair<K>(key1, key2));
    }

    public T getScore(K key1, K key2){
        return map.get(new UnorderedPair<K>(key1, key2));
    }

    public void set(K key1, K key2, T score){
        this.map.put(new UnorderedPair<K>(key1, key2), score);
    }

    public Iterator<Map.Entry<UnorderedPair<K>, T>> iterator() {
        return map.entrySet().iterator();
    }
}
