package org.ikora.analytics.visitor;

import org.ikora.model.*;

public class SizeVisitor extends EmptyVisitor {
    private int size;

    public SizeVisitor(){
        size = 0;
    }

    public int getSize(){
        return size;
    }

    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        ++size;
        VisitorUtils.traverseSteps(this, testCase, memory);
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        ++size;
        VisitorUtils.traverseSteps(this, keyword, memory);
    }

    @Override
    public void visit(LibraryKeyword keyword, VisitorMemory memory) {
        ++size;
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
    public void visit(Argument argument, VisitorMemory memory) {
        VisitorUtils.traverseArgument(this, argument, memory);
    }
}
