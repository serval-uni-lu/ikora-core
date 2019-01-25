package org.ukwikora.analytics;

import org.ukwikora.model.*;

import java.util.HashSet;
import java.util.Set;

public class FindTestCaseVisitor implements StatementVisitor {
    private Set<TestCase> testCases;

    public FindTestCaseVisitor(){
        testCases = new HashSet<>();
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
        visit((Statement)keyword);
    }

    @Override
    public void visit(KeywordCall call) {
        visit((Statement)call);
    }

    @Override
    public void visit(Assignment assignment) {
        visit((Statement)assignment);
    }

    @Override
    public void visit(ForLoop forLoop) {
        visit((Statement)forLoop);
    }

    @Override
    public void visit(LibraryKeyword keyword) {
        visit((Statement)keyword);
    }

    @Override
    public void visit(ScalarVariable scalar) {
        visit((Statement)scalar);
    }

    @Override
    public void visit(DictionaryVariable dictionary) {
        visit((Statement)dictionary);
    }

    @Override
    public void visit(ListVariable list) {
        visit((Statement)list);
    }

    private void visit(Statement statement){
        for(Statement dependency: statement.getDependencies()){
            dependency.accept(this);
        }
    }
}
