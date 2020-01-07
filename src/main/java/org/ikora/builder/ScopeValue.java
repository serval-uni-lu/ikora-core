package org.ikora.builder;

import org.ikora.analytics.visitor.FindSuiteVisitor;
import org.ikora.analytics.visitor.FindTestCaseVisitor;
import org.ikora.analytics.PathMemory;
import org.ikora.model.KeywordDefinition;
import org.ikora.model.TestCase;
import org.ikora.model.Value;

import java.util.Set;

class ScopeValue {
    Value value;
    KeywordDefinition keyword;

    ScopeValue(KeywordDefinition keyword, Value value){
        this.value = value;
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