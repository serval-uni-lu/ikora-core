package org.ikora.analytics.visitor;

import org.ikora.model.*;

public class EmptyVisitor implements NodeVisitor{
    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(Assignment assignment, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(ForLoop forLoop, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(LibraryKeyword keyword, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(ScalarVariable scalar, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(DictionaryVariable dictionary, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(ListVariable list, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(TimeOut timeOut, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(Argument argument, VisitorMemory memory) {
        //nothing to do in this node
    }
}
