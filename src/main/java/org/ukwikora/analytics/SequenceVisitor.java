package org.ukwikora.analytics;

import org.ukwikora.model.*;


public class SequenceVisitor implements StatementVisitor {
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

    @Override
    public void visit(LibraryKeyword keyword, VisitorMemory memory) {

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

    public Sequence getSequence(){
        return sequence;
    }

    public int getSequenceSize(){
        return sequence.size();
    }
}
