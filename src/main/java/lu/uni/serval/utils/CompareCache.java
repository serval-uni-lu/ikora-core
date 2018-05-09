package lu.uni.serval.utils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class CompareCache<K, T> implements Iterable<Map.Entry<EasyPair<K, K>, T>> {
    private Map<EasyPair<K, K>, T> map;
    private Queue<EasyPair<K,K>> queue;
    private static int maximumSize = 1000000;

    public CompareCache(){
        this.map = new HashMap<>();
        this.queue = new LinkedBlockingQueue<>(maximumSize + 1);
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
        EasyPair<K, K> pair = new EasyPair<>(key1, key2);

        this.map.put(pair, score);

        if(this.queue.size() >= maximumSize){
            this.map.remove(this.queue.poll());
        }

        this.queue.add(pair);
    }

    @Nonnull
    public Iterator<Map.Entry<EasyPair<K,K>, T>> iterator() {
        return map.entrySet().iterator();
    }
}
