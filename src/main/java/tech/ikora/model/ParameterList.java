package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.runner.Runtime;
import tech.ikora.types.BaseTypeFactory;
import tech.ikora.types.BaseTypeList;

import java.util.*;

public class ParameterList extends SourceNode implements List<Variable> {
    private final Token tag;
    private final List<Variable> parameters;
    private final BaseTypeList baseTypes;

    public ParameterList(){
        this(Token.empty());
    }

    public ParameterList(Token tag) {
        this.tag = tag.setType(Token.Type.LABEL);
        addToken(this.tag);

        this.parameters = new ArrayList<>();
        this.baseTypes = new BaseTypeList();
    }

    public BaseTypeList getBaseTypes(){
        return baseTypes;
    }

    @Override
    public int size() {
        return parameters.size();
    }

    @Override
    public boolean isEmpty() {
        return parameters.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return parameters.contains(o);
    }

    @Override
    public Iterator<Variable> iterator() {
        return parameters.iterator();
    }

    @Override
    public Object[] toArray() {
        return parameters.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return parameters.toArray(a);
    }

    @Override
    public boolean add(Variable variable) {
        boolean success = parameters.add(variable);

        if(success){
            baseTypes.add(BaseTypeFactory.fromVariable(variable));
            this.addAstChild(variable);
            this.addToken(variable.getNameToken());
        }

        return success;
    }

    @Override
    public boolean remove(Object o) {
        int index = parameters.indexOf(o);
        if(index < 0) return false;

        this.removeAstChild((Variable)o);
        baseTypes.remove(index);
        return parameters.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return parameters.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Variable> c) {
        for(Variable variable: c){
            add(variable);
        }

        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Variable> c) {
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
        baseTypes.clear();
        parameters.clear();
    }

    @Override
    public Variable get(int index) {
        return parameters.get(index);
    }

    @Override
    public Variable set(int index, Variable variable) {
        baseTypes.set(index, BaseTypeFactory.fromVariable(variable));
        return parameters.set(index, variable);
    }

    @Override
    public void add(int index, Variable variable) {
        baseTypes.add(index, BaseTypeFactory.fromVariable(variable));
        parameters.add(index, variable);
    }

    @Override
    public Variable remove(int index) {
        baseTypes.remove(index);
        return parameters.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return parameters.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return parameters.lastIndexOf(o);
    }

    @Override
    public ListIterator<Variable> listIterator() {
        return parameters.listIterator();
    }

    @Override
    public ListIterator<Variable> listIterator(int index) {
        return parameters.listIterator(index);
    }

    @Override
    public List<Variable> subList(int fromIndex, int toIndex) {
        return parameters.subList(fromIndex, toIndex);
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
