package org.ukwikora.analytics;

import org.ukwikora.model.*;

public class ConnectivityVisitor implements StatementVisitor {
    @Override
    public void visit(TestCase testCase) {

    }

    @Override
    public void visit(UserKeyword keyword) {

    }

    @Override
    public void visit(KeywordCall call) {

    }

    @Override
    public void visit(Assignment assignment) {

    }

    @Override
    public void visit(ForLoop forLoop) {

    }

    @Override
    public void visit(LibraryKeyword keyword) {

    }

    @Override
    public void visit(ScalarVariable scalar) {

    }

    @Override
    public void visit(DictionaryVariable dictionary) {

    }

    @Override
    public void visit(ListVariable list) {

    }

    @Override
    public boolean isAcceptable(Statement statement) {
        return false;
    }

    @Override
    public void process(Statement statement) {

    }
}
