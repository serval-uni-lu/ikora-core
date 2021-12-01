package lu.uni.serval.ikora.core.utils;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.*;

public class Permutations {
    private Permutations() {}

    public static <T> Collection<List<T>> permutations(List<Set<T>> lists) {
        if (lists == null || lists.isEmpty()) {
            return Collections.emptyList();
        }

        Collection<List<T>> res = new LinkedList<>();
        permutationsImpl(lists, res, 0, new LinkedList<>());

        return res;
    }

    private static <T> void permutationsImpl(List<Set<T>> origin, Collection<List<T>> res, int depth, List<T> current) {
        if (depth == origin.size()) {
            res.add(current);
            return;
        }

        Collection<T> currentCollection = origin.get(depth);

        for (T element : currentCollection) {
            List<T> copy = new LinkedList<>(current);
            copy.add(element);
            permutationsImpl(origin, res, depth + 1, copy);
        }
    }
}
