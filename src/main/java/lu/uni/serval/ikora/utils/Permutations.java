package lu.uni.serval.ikora.utils;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Permutations {


    public static <T> Collection<List<T>> permutations(List<List<T>> lists) {
        if (lists == null || lists.isEmpty()) {
            return Collections.emptyList();
        }

        Collection<List<T>> res = Lists.newLinkedList();
        permutationsImpl(lists, res, 0, new LinkedList<>());

        return res;
    }

    private static <T> void permutationsImpl(List<List<T>> origin, Collection<List<T>> res, int depth, List<T> current) {
        if (depth == origin.size()) {
            res.add(current);
            return;
        }

        Collection<T> currentCollection = origin.get(depth);

        for (T element : currentCollection) {
            List<T> copy = Lists.newLinkedList(current);
            copy.add(element);
            permutationsImpl(origin, res, depth + 1, copy);
        }
    }
}
