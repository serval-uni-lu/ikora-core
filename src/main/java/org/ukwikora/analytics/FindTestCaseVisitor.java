package org.ukwikora.analytics;

import org.ukwikora.model.*;
import org.ukwikora.utils.VisitorUtils;

import java.util.HashSet;
import java.util.Set;

public class FindTestCaseVisitor implements StatementVisitor {
    private Set<TestCase> testCases;
    private Set<Statement> memory;

    public FindTestCaseVisitor(){
        testCases = new HashSet<>();
        memory = new HashSet<>();
    }

    public Set<TestCase> getTestCases(){
        return testCases;
    }

    @Override
    public void visit(TestCase testCase) {
        testCases.add(testCase);
    }

    @Override
    public void visit(UserKeyword keyword) {
        VisitorUtils.traverseDependencies(this, keyword);
    }

    @Override
    public void visit(KeywordCall call) {
        VisitorUtils.traverseDependencies(this, call);
    }

    @Override
    public void visit(Assignment assignment) {
        VisitorUtils.traverseDependencies(this, assignment);
    }

    @Override
    public void visit(ForLoop forLoop) {
        VisitorUtils.traverseDependencies(this, forLoop);
    }

    @Override
    public void visit(LibraryKeyword keyword) {
        VisitorUtils.traverseDependencies(this, keyword);
    }

    @Override
    public void visit(ScalarVariable scalar) {
        VisitorUtils.traverseDependencies(this, scalar);
    }

    @Override
    public void visit(DictionaryVariable dictionary) {
        VisitorUtils.traverseDependencies(this, dictionary);
    }

    @Override
    public void visit(ListVariable list) {
        VisitorUtils.traverseDependencies(this, list);
    }

    @Override
    public boolean isAcceptable(Statement statement) {
        return memory.add(statement);
    }
}
