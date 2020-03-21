package tech.ikora.analytics.visitor;

import org.apache.commons.lang3.NotImplementedException;
import tech.ikora.model.*;

public class TreeVisitor implements NodeVisitor {
    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        VisitorUtils.traverseSteps(this, testCase, memory);
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        VisitorUtils.traverseSteps(this, keyword, memory);
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        VisitorUtils.traverseKeywordCall(this, call, memory);
    }

    @Override
    public void visit(Assignment assignment, VisitorMemory memory) {
        VisitorUtils.traverseAssignmentCall(this, assignment, memory);
    }

    @Override
    public void visit(ForLoop forLoop, VisitorMemory memory) {
        VisitorUtils.traverseForLoopSteps(this, forLoop, memory);
    }

    @Override
    public void visit(LibraryKeyword keyword, VisitorMemory memory) {
        //root node
    }

    @Override
    public void visit(ScalarVariable scalar, VisitorMemory memory) {
        //root node
    }

    @Override
    public void visit(DictionaryVariable dictionary, VisitorMemory memory) {
        //root node
    }

    @Override
    public void visit(ListVariable list, VisitorMemory memory) {
        //root node
    }

    @Override
    public void visit(TimeOut timeOut, VisitorMemory memory) {
        //root node
    }

    @Override
    public void visit(Argument argument, VisitorMemory memory) {
        VisitorUtils.traverseArgument(this, argument, memory);
    }

    @Override
    public <T extends Node> void visit(NodeTable<T> ts, VisitorMemory memory) {
        throw new NotImplementedException("Cannot use a tree visitor to traverse a node table yet");
    }
}
