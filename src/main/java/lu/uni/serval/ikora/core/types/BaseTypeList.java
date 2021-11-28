package lu.uni.serval.ikora.core.types;

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
import java.util.stream.Collectors;

public class BaseTypeList implements List<BaseType> {
    private final List<BaseType> argumentTypes;

    public BaseTypeList(){
        this.argumentTypes = new ArrayList<>();
    }

    public BaseTypeList(List<BaseType> argumentTypes) {
        this.argumentTypes =argumentTypes;
    }

    @Override
    public int size() {
        return argumentTypes.size();
    }

    @Override
    public boolean isEmpty() {
        return argumentTypes.isEmpty();
    }

    @Override
    public boolean contains(Object type) {
        return argumentTypes.contains(type);
    }

    @Override
    public Iterator<BaseType> iterator() {
        return argumentTypes.iterator();
    }

    @Override
    public Object[] toArray() {
        return argumentTypes.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return argumentTypes.toArray(a);
    }

    @Override
    public boolean add(BaseType type) {
        return argumentTypes.add(type);
    }

    @Override
    public boolean remove(Object type) {
        return argumentTypes.remove(type);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return argumentTypes.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends BaseType> c) {
        return argumentTypes.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends BaseType> c) {
        return argumentTypes.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return argumentTypes.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return argumentTypes.retainAll(c);
    }

    @Override
    public void clear() {
        argumentTypes.clear();
    }

    @Override
    public BaseType get(int index) {
        return argumentTypes.get(index);
    }

    @Override
    public BaseType set(int index, BaseType type) {
        return argumentTypes.set(index, type);
    }

    @Override
    public void add(int index, BaseType element) {
        argumentTypes.add(index, element);
    }

    @Override
    public BaseType remove(int index) {
        return argumentTypes.remove(index);
    }

    @Override
    public int indexOf(Object type) {
        return argumentTypes.indexOf(type);
    }

    @Override
    public int lastIndexOf(Object type) {
        return argumentTypes.lastIndexOf(type);
    }

    @Override
    public ListIterator<BaseType> listIterator() {
        return argumentTypes.listIterator();
    }

    @Override
    public ListIterator<BaseType> listIterator(int index) {
        return argumentTypes.listIterator(index);
    }

    @Override
    public List<BaseType> subList(int fromIndex, int toIndex) {
        return argumentTypes.subList(fromIndex, toIndex);
    }

    public <T extends BaseType> boolean containsType(Class<T> type){
        return argumentTypes.stream().anyMatch(t -> type.isAssignableFrom(t.getClass()));
    }

    public int findFirst(Class<?> type){
        return argumentTypes.stream()
                .filter(t -> type.isAssignableFrom(t.getClass()))
                .findFirst()
                .map(this::indexOf)
                .orElse(-1);
    }

    public List<Integer> findAll(Class<?> type){
        return argumentTypes.stream()
                .filter(t -> type.isAssignableFrom(t.getClass()))
                .map(this::indexOf)
                .collect(Collectors.toList());
    }
}
