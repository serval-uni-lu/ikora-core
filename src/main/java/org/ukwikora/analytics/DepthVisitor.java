package org.ukwikora.analytics;

import org.ukwikora.model.*;

public class DepthVisitor implements StatementVisitor {
    int depth = 0;

    private void updateDepth(VisitorMemory memory){
        if(memory instanceof DepthMemory){
            int size = ((DepthMemory)memory).getDepth();
            this.depth = Math.max(size, this.depth);
        }
    }

    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        updateDepth(memory);
        VisitorUtils.traverseSteps(this, testCase, memory);
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        updateDepth(memory);
        VisitorUtils.traverseSteps(this, keyword, memory);
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        updateDepth(memory);
        VisitorUtils.traverseKeywordCall(this, call, memory);
    }

    @Override
    public void visit(Assignment assignment, VisitorMemory memory) {
        updateDepth(memory);
        VisitorUtils.traverseAssignmentCall(this, assignment, memory);
    }

    @Override
    public void visit(ForLoop forLoop, VisitorMemory memory) {
        updateDepth(memory);
        VisitorUtils.traverseForLoopSteps(this, forLoop, memory);
    }

    @Override
    public void visit(LibraryKeyword keyword, VisitorMemory memory) {
        updateDepth(memory);
    }

    @Override
    public void visit(ScalarVariable scalar, VisitorMemory memory) {

    }

    @Override
    public void visit(DictionaryVariable dictionary, VisitorMemory memory) {

    }

    @Override
    public void visit(ListVariable list, VisitorMemory memory) {

    }
}
