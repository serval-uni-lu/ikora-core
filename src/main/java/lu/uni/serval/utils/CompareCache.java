package lu.uni.serval.utils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CompareCache<K, T> implements Iterable<Map.Entry<EasyPair<K, K>, T>> {
    private Map<EasyPair<K, K>, T> map;

    public CompareCache(){
        this.map = new HashMap<>();
    }

    public int size(){
        return this.map.size();
    }

    public boolean isCached(K key1, K key2){
        return map.containsKey(new EasyPair<>(key1, key2));
    }

    public T getScore(K key1, K key2){
        return map.get(new EasyPair<>(key1, key2));
    }

    public void set(K key1, K key2, T score){
        this.map.put(new EasyPair<>(key1, key2), score);
    }

    @Nonnull
    public Iterator<Map.Entry<EasyPair<K,K>, T>> iterator() {
        return map.entrySet().iterator();
    }
}
