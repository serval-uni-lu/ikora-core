package org.ikora.analytics.visitor;

import org.ikora.model.*;


public class SequenceVisitor extends EmptyVisitor {
    private Sequence sequence;

    public SequenceVisitor(){
        sequence = new Sequence();
    }

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
        call.getKeyword().ifPresent(keyword -> {
            if(LibraryKeyword.class.isAssignableFrom(keyword.getClass())){
                sequence.addStep(call);
            }
        });

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

    public Sequence getSequence(){
        return sequence;
    }

    public int getSequenceSize(){
        return sequence.size();
    }
}
