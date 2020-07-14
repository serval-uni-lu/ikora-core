package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.runner.Runtime;

import java.util.*;

public class NodeList<N extends SourceNode> extends SourceNode implements List<N> , HiddenAstNode{
    private final Token tag;
    private final List<N> nodes;

    public NodeList(){
        this(Token.empty());
    }

    public NodeList(List<N> nodes){
        this.tag = Token.empty();

        this.nodes = new ArrayList<>();
        this.addAll(nodes);
    }

    public NodeList(Token tag) {
        this.tag = tag.setType(Token.Type.LABEL);
        addToken(this.tag);

        this.nodes = new ArrayList<>();
    }

    @Override
    public int size() {
        return nodes.size();
    }

    @Override
    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return nodes.contains(o);
    }

    @Override
    public Iterator<N> iterator() {
        return nodes.iterator();
    }

    @Override
    public Object[] toArray() {
        return nodes.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return nodes.toArray(a);
    }

    @Override
    public boolean add(N node) {
        boolean success = nodes.add(node);

        if(success){
            this.addAstChild(node);
            this.addToken(node.getNameToken());
        }

        return success;
    }

    @Override
    public boolean remove(Object o) {
        return nodes.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return nodes.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends N> c) {
        for(N node: c){
            add(node);
        }

        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends N> c) {
        throw new UnsupportedOperationException("This addAll(int, Collection) is not supported by the ParameterList");
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
        throw new UnsupportedOperationException("This retainAll(Collection) is not supported by the ParameterList");
    }

    @Override
    public void clear() {
        nodes.clear();
    }

    @Override
    public N get(int index) {
        return nodes.get(index);
    }

    @Override
    public N set(int index, N node) {
        return nodes.set(index, node);
    }

    @Override
    public void add(int index, N node) {
        nodes.add(index, node);
    }

    @Override
    public N remove(int index) {
        return nodes.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return nodes.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return nodes.lastIndexOf(o);
    }

    @Override
    public ListIterator<N> listIterator() {
        return nodes.listIterator();
    }

    @Override
    public ListIterator<N> listIterator(int index) {
        return nodes.listIterator(index);
    }

    @Override
    public List<N> subList(int fromIndex, int toIndex) {
        return nodes.subList(fromIndex, toIndex);
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
        return this.tag.equalsIgnorePosition(name);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {

    }

    @Override
    public void execute(Runtime runtime) throws Exception {

    }
}
