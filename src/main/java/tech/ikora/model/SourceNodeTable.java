package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.runner.Runtime;
import org.apache.commons.lang3.NotImplementedException;

import java.util.*;
import java.util.stream.Collectors;

public class SourceNodeTable<T extends SourceNode> extends SourceNode implements Iterable<T> {
    private Token header;
    private List<T> nodeList;
    private SourceFile sourceFile;

    public SourceNodeTable() {
        this.header = Token.empty();
        this.nodeList = new ArrayList<>();
    }

    private SourceNodeTable(Token header, SourceFile sourceFile, List<T> nodeList){
        this.header = header;
        this.sourceFile = sourceFile;
        this.nodeList = new ArrayList<>(nodeList);
    }

    public SourceNodeTable<T> clone(){
        return new SourceNodeTable<>(header, sourceFile, nodeList);
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
        return this.equals(other) ? 0. : 1.;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        return null;
    }
}
