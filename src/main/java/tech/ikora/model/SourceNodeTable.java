package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.runner.Runtime;
import org.apache.commons.lang3.NotImplementedException;
import tech.ikora.utils.LevenshteinDistance;

import java.util.*;
import java.util.stream.Collectors;

public class SourceNodeTable<T extends SourceNode> extends SourceNode implements Iterable<T> {
    private Token header;
    private List<T> nodeList;

    public SourceNodeTable() {
        this.header = Token.empty();
        this.nodeList = new ArrayList<>();
    }

    private SourceNodeTable(Token header, List<T> nodeList){
        this.header = header;
        this.nodeList = new ArrayList<>(nodeList);
    }

    @Override
    public SourceNodeTable<T> clone(){
        return new SourceNodeTable<>(header, nodeList);
    }

    public void setHeader(Token header){
        this.header = header;
    }

    public Token getHeader(){
        return header;
    }

    public Set<T> findNode(T node){
        return findNode(node.getLibraryName(), node.getNameToken());
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
        nodeList = nodeList.stream().filter(n -> n != node).collect(Collectors.toList());
    }

    @Override
    public boolean matches(Token name) {
        return getNameToken().equalsIgnorePosition(name);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public void execute(Runtime runtime) throws Exception {
        throw new NotImplementedException("Note Table execution not implemented");
    }

    @Override
    public Token getNameToken() {
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
    public double distance(Differentiable other) {
        if(other == this){
            return 0.;
        }

        if(other == null || other.getClass() != this.getClass()){
            return 1.;
        }

        SourceNodeTable<T> nodeTable = (SourceNodeTable<T>)other;

        double distance = this.header.equalsIgnorePosition(nodeTable.header) ? 0. : 0.5;
        distance += LevenshteinDistance.index(this.nodeList, nodeTable.nodeList) * 0.5;

        return distance;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        if(other == this){
            return Collections.emptyList();
        }

        List<Action> actions = new ArrayList<>();

        if(other == null){
            actions.add(Action.removeElement(SourceNodeTable.class, this));
            return actions;
        }

        if(other.getClass() != this.getClass()){
            actions.add(Action.changeType(this, other));
        }

        SourceNodeTable<T> nodeTable = (SourceNodeTable<T>)other;

        if(!this.header.equalsIgnorePosition(nodeTable.header)){
            actions.add(Action.changeName(this, nodeTable));
        }

        actions.addAll(LevenshteinDistance.getDifferences(this.nodeList, nodeTable.nodeList));

        return actions;
    }
}
