package lu.uni.serval.ikora.core.model;

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

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.exception.RunnerException;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.utils.LevenshteinDistance;
import org.apache.commons.lang3.NotImplementedException;

import java.util.*;
import java.util.stream.Stream;

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
    public Stream<N> stream(){
        return nodes.stream();
    }

    @Override
    public boolean add(N node) {
        nodes.add(node);

        this.addAstChild(node);
        this.addToken(node.getDefinitionToken());

        return true;
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
    public Token getDefinitionToken() {
        return this.tag;
    }

    @Override
    public double distance(SourceNode other) {
        if(this.getClass() == other.getClass()){
            return 1.;
        }

        return LevenshteinDistance.index(this, (NodeList<N>)other);
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null){
            return Collections.singletonList(Edit.removeElement(this.getClass(), this));
        }

        if(other == this){
            return Collections.emptyList();
        }

        return LevenshteinDistance.getDifferences(this, (NodeList<N>)other);
    }

    @Override
    public boolean matches(Token name) {
        return this.tag.matches(name);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public void execute(Runtime runtime) throws RunnerException {
        throw new NotImplementedException("Runner is not implemented yet");
    }
}
