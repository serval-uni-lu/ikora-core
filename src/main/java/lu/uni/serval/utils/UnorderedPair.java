package lu.uni.serval.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnorderedPair<T> {
    private final Set<T> set;

    public UnorderedPair(T a, T b) {
        set = new HashSet<T>();
        set.add(a);
        set.add(b);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof UnorderedPair && set.equals(((UnorderedPair<T>)other).set);
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }

    public T first(){
        return set.iterator().next();
    }

    public T second(){
        set.iterator().next();
        return set.iterator().next();
    }
}