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
import java.util.stream.Collectors;

public class SourceNodeTable<T extends SourceNode> extends SourceNode implements Iterable<T> {
    private Token header;
    private NodeList<T> nodeList;

    public SourceNodeTable() {
        this.header = Token.empty();
        this.nodeList = new NodeList<>();
        this.addAstChild(this.nodeList);
    }

    private SourceNodeTable(Token header, List<T> nodeList){
        this.header = header;
        this.nodeList = new NodeList<>(nodeList);
        this.addAstChild(this.nodeList);
    }

    public SourceNodeTable<T> copy(){
        return new SourceNodeTable<>(header, nodeList);
    }

    public void setHeader(Token header){
        this.header = header;
    }

    public Token getHeader(){
        return header;
    }

    public Set<T> findNode(T node){
        return findNode(node.getLibraryName(), node.getDefinitionToken());
    }

    public Set<T> findNode(Token token){
        return findNode(null, token);
    }

    public Set<T> findNode(String file, Token token){
        Set<T> nodes = new HashSet<>();

        for(T node: nodeList){
            if(matches(file, token, node)){
                nodes.add(node);
            }
        }

        return nodes;
    }

    @Override
    public Tokens getTokens(){
        Tokens tokens = new Tokens();
        tokens.add(header);
        nodeList.forEach(node -> tokens.addAll(node.getTokens()));
        return tokens;
    }

    public void remove(T node){
        nodeList = nodeList.stream().filter(n -> n != node).collect(Collectors.toCollection(NodeList::new));
        this.addAstChild(nodeList);
    }

    @Override
    public boolean matches(Token name) {
        return getDefinitionToken().matches(name);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public void execute(Runtime runtime) throws RunnerException {
        throw new NotImplementedException("Note Table execution not implemented");
    }

    @Override
    public Token getDefinitionToken() {
        return this.header;
    }

    private boolean matches(String file, Token token, T node){
        if(file == null){
            return node.matches(token);
        }

        return file.equalsIgnoreCase(node.getLibraryName()) && node.matches(token);
    }

    public int size() {
        return this.nodeList.size();
    }

    public boolean isEmpty() {
        return this.nodeList.isEmpty();
    }

    public boolean contains(T node) {
        if(node == null){
            return false;
        }

        return this.nodeList.contains(node);
    }

    @Override
    public Iterator<T> iterator() {
        return this.nodeList.iterator();
    }

    public List<T> asList() {
        return nodeList;
    }

    public void add(T node) {
        if(node == null || nodeList.contains(node)){
            return;
        }

        nodeList.add(node);
        this.addAstChild(node);
    }

    public void addAll(SourceNodeTable<T> nodeTable) {
        for(T node: nodeTable){
            add(node);
        }
    }

    public void addAll(Collection<T> nodes){
        for(T node: nodes){
            add(node);
        }
    }

    public void clear() {
        this.nodeList.clear();
    }

    @Override
    public double distance(SourceNode other) {
        if(other == this){
            return 0.;
        }

        if(other == null || other.getClass() != this.getClass()){
            return 1.;
        }

        SourceNodeTable<T> nodeTable = (SourceNodeTable<T>)other;

        double distance = this.header.matches(nodeTable.header) ? 0. : 0.5;
        distance += LevenshteinDistance.index(this.nodeList, nodeTable.nodeList) * 0.5;

        return distance;
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null){
            return Collections.singletonList(Edit.removeElement(this.getClass(), this));
        }

        if(other == this){
            return Collections.emptyList();
        }

        List<Edit> edits = new ArrayList<>();

        if(other.getClass() != this.getClass()){
            edits.add(Edit.changeType(this, other));
        }

        SourceNodeTable<T> nodeTable = (SourceNodeTable<T>)other;

        if(!this.header.matches(nodeTable.header)){
            edits.add(Edit.changeName(this, nodeTable));
        }

        edits.addAll(LevenshteinDistance.getDifferences(this.nodeList, nodeTable.nodeList));

        return edits;
    }
}
