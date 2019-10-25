package org.ukwikora.builder;

import org.ukwikora.analytics.FindSuiteVisitor;
import org.ukwikora.analytics.FindTestCaseVisitor;
import org.ukwikora.analytics.PathMemory;
import org.ukwikora.model.KeywordDefinition;
import org.ukwikora.model.TestCase;
import org.ukwikora.model.Value;

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