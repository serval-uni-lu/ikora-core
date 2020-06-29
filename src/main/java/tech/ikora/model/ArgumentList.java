package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.runner.Runtime;

import java.util.*;

public class ArgumentList extends SourceNode implements List<Argument> {
    private final Token tag;
    private final List<Argument> arguments;

    public ArgumentList(){
        this.tag = Token.empty();
        this.arguments = new ArrayList<>();
    }

    public ArgumentList(Token tag){
        this.tag = tag;
        this.arguments = new ArrayList<>();
    }

    public ArgumentList(List<Argument> arguments) {
        this.tag = Token.empty();
        this.arguments = arguments;
    }

    public ArgumentList(Token tag, List<Argument> arguments) {
        this.tag = tag;
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
        this.addAstChild(argument);
        return arguments.add(argument);
    }

    @Override
    public boolean remove(Object o) {
        if(arguments.contains(o)){
            this.removeAstChild((Argument)o);
        }

        return arguments.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return arguments.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Argument> c) {
        boolean success = true;

        for(Argument argument: c){
            success &= add(argument);
        }

        return success;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Argument> c) {
        throw new UnsupportedOperationException("This addAll(Collection) is not supported by the ArgumentList");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean success = true;

        for(Object o: c){
            success &= remove(o);
        }

        return success;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("This retainAll(Collection) is not supported by the ArgumentList");
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
    public void add(int index, Argument argument) {
        this.addAstChild(argument);
        arguments.add(index, argument);
    }

    @Override
    public Argument remove(int index) {
        Argument argument = arguments.get(index);
        remove(argument);

        return argument;
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

    public int findFirst(Class<?> type){
        final Optional<Argument> first = arguments.stream()
                .filter(a -> a.getDefinition()
                        .map(n -> type.isAssignableFrom(n.getClass()))
                        .orElse(false))
                .findFirst();

        return first.map(arguments::indexOf).orElse(-1);
    }

    public boolean isExpendedUntilPosition(int position){
        int listIndex = findFirst(ListVariable.class);
        int dictIndex = findFirst(DictionaryVariable.class);

        int varIndex = size();
        varIndex = listIndex != -1 ? Math.min(varIndex, listIndex) : varIndex;
        varIndex = dictIndex != -1 ? Math.min(varIndex, dictIndex) : varIndex;

        return position < varIndex;
    }

    @Override
    public Token getNameToken() {
        return this.tag;
    }

    @Override
    public double distance(Differentiable other) {
        return 0;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        return null;
    }

    @Override
    public boolean matches(Token name) {
        if(this.tag.isEmpty()){
            return false;
        }

        return this.tag.equalsIgnorePosition(name);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {

    }

    @Override
    public void execute(Runtime runtime) throws Exception {

    }
}
