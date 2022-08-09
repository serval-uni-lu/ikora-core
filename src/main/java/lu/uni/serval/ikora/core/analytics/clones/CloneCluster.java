/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.analytics.clones;

import lu.uni.serval.ikora.core.model.SourceNode;

import java.util.*;

public class CloneCluster<T extends SourceNode> implements Collection<T> {
    private final Clones.Type type;
    private final Set<T> clones;

    private boolean isCrossProject;

    public CloneCluster(Clones.Type type) {
        this.type = type;
        this.clones = new HashSet<>();
        this.isCrossProject = false;
    }

    public Clones.Type getType() {
        return type;
    }

    public boolean isCrossProject() {
        return isCrossProject;
    }

    @Override
    public int size() {
        return clones.size();
    }

    @Override
    public boolean isEmpty() {
        return clones.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return clones.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return clones.iterator();
    }

    @Override
    public Object[] toArray() {
        return clones.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return clones.toArray(a);
    }

    @Override
    public boolean add(T t){
        boolean result = clones.add(t);
        update();

        return result;
    }

    @Override
    public boolean remove(Object o) {
        boolean result = clones.remove(o);
        update();

        return result;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean result = clones.containsAll(c);
        update();

        return result;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean result = clones.addAll(c);
        update();

        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = clones.removeAll(c);
        update();

        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean result = clones.retainAll(c);
        update();

        return result;
    }

    @Override
    public void clear() {
        clones.clear();
        update();
    }

    private void update(){
        this.isCrossProject = this.clones.stream()
                .map(t -> t.getProject().getName())
                .distinct()
                .count() > 1;
    }
}
