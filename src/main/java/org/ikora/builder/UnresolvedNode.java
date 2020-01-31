package org.ikora.builder;

import org.ikora.analytics.visitor.FindSuiteVisitor;
import org.ikora.analytics.visitor.FindTestCaseVisitor;
import org.ikora.analytics.PathMemory;
import org.ikora.model.*;

import java.io.File;
import java.util.Set;

class UnresolvedNode {
    private Node node;
    private KeywordDefinition parent;

    UnresolvedNode(KeywordDefinition parent, Node node){
        this.node = node;
        this.parent = parent;
    }

    public Token getName(){
        return node.getName();
    }

    public Node getNode(){
        return node;
    }

    public KeywordDefinition getParent() {
        return parent;
    }

    public File getFile(){
        return parent != null ? parent.getFile() : null;
    }

    Set<TestCase> getTestCases(){
        FindTestCaseVisitor visitor = new FindTestCaseVisitor();
        this.parent.accept(visitor, new PathMemory());

        return visitor.getTestCases();
    }

    Set<String> getSuites(){
        FindSuiteVisitor visitor = new FindSuiteVisitor();
        this.parent.accept(visitor, new PathMemory());

        return visitor.getSuites();
    }
}