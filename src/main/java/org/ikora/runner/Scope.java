package org.ikora.runner;


import org.ikora.model.*;

import java.util.List;
import java.util.Set;

public interface Scope {
    void addToKeyword(Keyword keyword, Variable variable);
    void addToTest(TestCase testCase, Variable variable);
    void addToSuite(String suite, Variable variable);
    void addToGlobal(Variable variable);

    Set<Node> findInScope(Set<TestCase> testCases, Set<String> suites, Token name);
    
    void addDynamicLibrary(KeywordDefinition keyword, List<Argument> argumentList);
    ResourcesTable getDynamicResources(Node node);

    void enterNode(Node node) throws Exception;
    void exitNode(Node node);

    void enterSuite(Suite suite);
    void exitSuite(Suite suite);

    void reset();

    TestCase getTestCase();

    List<Variable> getReturnVariables();
}
