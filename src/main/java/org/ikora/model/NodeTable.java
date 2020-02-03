package org.ikora.model;

import java.io.File;
import java.util.*;

public class NodeTable<T extends Node> implements Iterable<T> {
    private Token name;
    private HashMap<String, T> nodeMap;
    private List<T> nodeList;
    private SourceFile file;

    public NodeTable() {
        this.nodeMap = new HashMap<>();
        this.nodeList = new ArrayList<>();
    }

    public void setName(Token name){
        this.name = name;
    }

    public void setFile(SourceFile file) {
        this.file = file;

        nodeMap = new HashMap<>();
        for (T node: nodeList){
            node.setSourceFile(this.file);
            nodeMap.put(getKey(node), node);
        }
    }

    public SourceFile getFile() {
        return file;
    }

    public NodeTable(NodeTable<T> other){
        nodeMap = other.nodeMap;
        nodeList = other.nodeList;

        file = other.file;
    }

    public Set<T> findNode(T node){
        return findNode(node.getFileName(), node.getName());
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
        tokens.add(name);
        nodeList.forEach(node -> tokens.addAll(node.getTokens()));
        return tokens;
    }

    private boolean matches(String file, Token token, T node){
        if(file == null){
            return node.matches(token);
        }

        return file.equalsIgnoreCase(node.getFileName()) && node.matches(token);
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

    public boolean add(T node) {
        if(node == null){
            return false;
        }

        if(nodeList.contains(node)){
            return false;
        }

        nodeList.add(node);
        nodeMap.put(getKey(node), node);

        return true;
    }

    public boolean remove(T node) {
        if(node == null){
            return false;
        }

        this.nodeMap.remove(getKey(node));
        this.nodeList.remove(node);

        return true;
    }

    public void extend(NodeTable<T> table) {
        for(T node: table){
            if(nodeList.contains(node)){
                continue;
            }

            this.nodeList.add(node);
            this.nodeMap.put(getKey(node), node);
        }
    }

    public void clear() {
        this.nodeList.clear();
        this.nodeMap.clear();
    }

    private String getKey(T node){
        return node.getSourceFile() + File.separator + node.getName();
    }
}
