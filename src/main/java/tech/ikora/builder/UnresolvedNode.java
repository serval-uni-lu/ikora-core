package tech.ikora.builder;

import tech.ikora.analytics.visitor.FindSuiteVisitor;
import tech.ikora.analytics.visitor.FindTestCaseVisitor;
import tech.ikora.analytics.PathMemory;
import tech.ikora.model.KeywordDefinition;
import tech.ikora.model.Node;
import tech.ikora.model.TestCase;
import tech.ikora.model.Token;

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