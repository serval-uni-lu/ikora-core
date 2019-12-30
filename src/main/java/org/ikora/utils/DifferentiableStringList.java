package org.ikora.utils;

import java.util.*;

public class DifferentiableStringList implements List<DifferentiableString> {
    private List<DifferentiableString> stringList;

    public DifferentiableStringList(){
        stringList = new ArrayList<>();
    }

    @Override
    public int size() {
        return stringList.size();
    }

    @Override
    public boolean isEmpty() {
        return stringList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return stringList.contains(o);
    }

    @Override
    public Iterator<DifferentiableString> iterator() {
        return stringList.iterator();
    }

    @Override
    public Object[] toArray() {
        return stringList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return stringList.toArray(a);
    }

    @Override
    public boolean add(DifferentiableString differentiable) {
        return stringList.add(differentiable);
    }

    public boolean add(String string){
        return stringList.add(new DifferentiableString(string));
    }

    @Override
    public boolean remove(Object o) {
        return stringList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return stringList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends DifferentiableString> c) {
        return stringList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends DifferentiableString> c) {
        return stringList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return stringList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return stringList.retainAll(c);
    }

    @Override
    public void clear() {
        stringList.clear();
    }

    @Override
    public DifferentiableString get(int index) {
        return stringList.get(index);
    }

    @Override
    public DifferentiableString set(int index, DifferentiableString element) {
        return stringList.set(index, element);
    }

    @Override
    public void add(int index, DifferentiableString element) {
        stringList.add(index, element);
    }

    @Override
    public DifferentiableString remove(int index) {
        return stringList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return stringList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return stringList.lastIndexOf(o);
    }

    @Override
    public ListIterator<DifferentiableString> listIterator() {
        return stringList.listIterator();
    }

    @Override
    public ListIterator<DifferentiableString> listIterator(int index) {
        return stringList.listIterator(index);
    }

    @Override
    public List<DifferentiableString> subList(int fromIndex, int toIndex) {
        return stringList.subList(fromIndex, toIndex);
    }

    public static DifferentiableStringList fromTextBlock(String block){
        DifferentiableStringList list = new DifferentiableStringList();

        for(String line: block.split(StringUtils.lineBreak.pattern())){
            list.add(line);
        }

        return list;
    }
}
