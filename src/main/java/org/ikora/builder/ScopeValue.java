package org.ikora.builder;

import org.ikora.analytics.visitor.FindSuiteVisitor;
import org.ikora.analytics.visitor.FindTestCaseVisitor;
import org.ikora.analytics.PathMemory;
import org.ikora.model.*;

import java.io.File;
import java.util.Set;

class ScopeValue {
    private Value value;
    private KeywordDefinition keyword;
    private Position position;

    ScopeValue(KeywordDefinition keyword, Value value, Position position){
        this.value = value;
        this.keyword = keyword;
        this.position = position;
    }

    public Token getName(){
        return value.getName();
    }

    public Value getValue() {
        return value;
    }

    public KeywordDefinition getKeyword() {
        return keyword;
    }

    public Position getPosition() {
        return position;
    }

    public File getFile(){
        return keyword != null ? keyword.getFile() : null;
    }

    Set<TestCase> getTestCases(){
        FindTestCaseVisitor visitor = new FindTestCaseVisitor();
        this.keyword.accept(visitor, new PathMemory());

        return visitor.getTestCases();
    }

    Set<String> getSuites(){
        FindSuiteVisitor visitor = new FindSuiteVisitor();
        this.keyword.accept(visitor, new PathMemory());

        return visitor.getSuites();
    }
}