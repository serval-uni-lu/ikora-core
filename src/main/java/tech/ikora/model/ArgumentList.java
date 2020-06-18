package tech.ikora.model;

import java.util.*;

public class ArgumentList  implements List<Argument> {
    private List<Argument> arguments;

    public ArgumentList(){
        this.arguments = new ArrayList<>();
    }

    public ArgumentList(List<Argument> arguments) {
        this.arguments = arguments;
    }

    @Override
    public int size() {
        return arguments.size();
    }

    @Override
    public boolean isEmpty() {
        return arguments.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return arguments.contains(o);
    }

    @Override
    public Iterator<Argument> iterator() {
        return arguments.iterator();
    }

    @Override
    public Object[] toArray() {
        return arguments.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return arguments.toArray(a);
    }

    @Override
    public boolean add(Argument argument) {
        return arguments.add(argument);
    }

    @Override
    public boolean remove(Object o) {
        return arguments.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return arguments.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Argument> c) {
        return arguments.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Argument> c) {
        return arguments.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return arguments.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return arguments.retainAll(c);
    }

    @Override
    public void clear() {
        arguments.clear();
    }

    @Override
    public Argument get(int index) {
        return arguments.get(index);
    }

    @Override
    public Argument set(int index, Argument element) {
        return arguments.set(index, element);
    }

    @Override
    public void add(int index, Argument element) {
        arguments.add(index, element);
    }

    @Override
    public Argument remove(int index) {
        return arguments.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return arguments.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return arguments.lastIndexOf(o);
    }

    @Override
    public ListIterator<Argument> listIterator() {
        return arguments.listIterator();
    }

    @Override
    public ListIterator<Argument> listIterator(int index) {
        return arguments.listIterator(index);
    }

    @Override
    public List<Argument> subList(int fromIndex, int toIndex) {
        return arguments.subList(fromIndex, toIndex);
    }
}
