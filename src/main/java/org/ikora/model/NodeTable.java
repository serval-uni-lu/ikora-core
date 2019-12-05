package org.ikora.model;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;

public class NodeTable<T extends Node> implements Iterable<T> {
    private HashMap<String, T> nodeMap;
    private List<T> nodeList;
    private SourceFile file;

    public NodeTable() {
        this.nodeMap = new HashMap<>();
        this.nodeList = new ArrayList<>();
    }

    public void setFile(SourceFile file) {
        this.file = file;

        nodeMap = new HashMap<>();
        for (T node: nodeList){
            node.setFile(this.file);
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

    public Set<T> findNode(String name){
        return findNode(null, name);
    }

    public Set<T> findNode(String file, String name){
        Set<T> nodes = new HashSet<>();

        for(T node: nodeList){
            if(matches(file, name, node)){
                nodes.add(node);
            }
        }

        return nodes;
    }

    private boolean matches(String file, String name, T node){
        if(file == null){
            return node.matches(name);
        }

        return file.equalsIgnoreCase(node.getFileName()) && node.matches(name);
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
    @Nonnull
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
        return node.getFile() + File.separator + node.getName();
    }
}
