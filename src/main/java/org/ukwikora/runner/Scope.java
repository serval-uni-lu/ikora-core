package org.ukwikora.runner;


import org.ukwikora.model.*;

import java.util.List;
import java.util.Set;

public interface Scope {
    void addToTest(TestCase testCase, Variable variable);
    void addToSuite(String suite, Variable variable);
    void addToGlobal(Variable variable);

    Set<Variable> findInScope(Set<TestCase> testCases, Set<String> suites, String name);
    
    void addDynamicLibrary(KeywordDefinition keyword, List<Value> parameters);
    ResourcesTable getDynamicResources(Statement statement);

    void enterKeyword(Keyword keyword) throws Exception;
    void exitKeyword(Keyword keyword);

    void enterSuite(Suite suite);
    void exitSuite(Suite suite);

    void reset();

    TestCase getTestCase();
}
