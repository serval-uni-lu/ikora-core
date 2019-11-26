package org.ukwikora.analytics;

import org.ukwikora.model.*;

import java.util.HashSet;
import java.util.Set;

public class FindTestCaseVisitor implements NodeVisitor {
    private Set<TestCase> testCases;

    public FindTestCaseVisitor(){
        testCases = new HashSet<>();
    }

    public Set<TestCase> getTestCases(){
        return testCases;
    }

    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        testCases.add(testCase);
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, keyword, memory);
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, call, memory);
    }

    @Override
    public void visit(Assignment assignment, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, assignment, memory);
    }

    @Override
    public void visit(ForLoop forLoop, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, forLoop, memory);
    }

    @Override
    public void visit(LibraryKeyword keyword, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, keyword, memory);
    }

    @Override
    public void visit(ScalarVariable scalar, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, scalar, memory);
    }

    @Override
    public void visit(DictionaryVariable dictionary, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, dictionary, memory);
    }

    @Override
    public void visit(ListVariable list, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, list, memory);
    }

    @Override
    public void visit(TimeOut timeOut, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, timeOut, memory);
    }
}
