package lu.uni.serval.utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class UnorderedPair<T> {
    private final Set<T> set;

    public UnorderedPair(T a, T b) {
        set = new HashSet<>();
        set.add(a);
        set.add(b);
    }

    public static <T> UnorderedPair<T> of(final T left, final T right) {
        return new UnorderedPair<>(left, right);
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
        Iterator<T> iterator = set.iterator();
        iterator.next();

        return iterator.next();
    }
}