package org.ukwikora.analytics;

import org.ukwikora.model.*;
import org.ukwikora.utils.VisitorUtils;

import java.util.HashSet;
import java.util.Set;

public class ConnectivityVisitor implements StatementVisitor {
    private int connectivity;
    private Set<Statement> memory;

    public ConnectivityVisitor() {
        this.connectivity = 0;
        this.memory = new HashSet<>();
    }

    public int getConnectivity(){
        return connectivity;
    }

    @Override
    public void visit(TestCase testCase) {

    }

    @Override
    public void visit(UserKeyword keyword) {
        connectivity += keyword.getDependencies().size() > 0 ? 1 : 0;
        VisitorUtils.traverseDependencies(this, keyword);
    }

    @Override
    public void visit(LibraryKeyword keyword) {
        connectivity += keyword.getDependencies().size() > 0 ? 1 : 0;
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
