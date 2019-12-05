package org.ikora.builder;

import org.ikora.analytics.FindSuiteVisitor;
import org.ikora.analytics.FindTestCaseVisitor;
import org.ikora.analytics.PathMemory;
import org.ikora.model.KeywordDefinition;
import org.ikora.model.TestCase;
import org.ikora.model.Value;

import java.util.Set;

class ScopeValue {
    Value value;
    String variableName;
    KeywordDefinition keyword;

    ScopeValue(KeywordDefinition keyword, Value value, String variableName){
        this.value = value;
        this.variableName = variableName;
        this.keyword = keyword;
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