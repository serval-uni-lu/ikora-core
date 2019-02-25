package org.ukwikora.analytics;

import org.ukwikora.model.*;

public class SizeVisitor implements StatementVisitor {
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
        if(call.getKeyword() == null){
            return;
        }

        if(call.hasKeywordParameters()){
            for(Step step: call.getKeywordParameter()){
                if(step == null){
                    continue;
                }

                step.accept(this, memory.getUpdated(step));
            }
        }

        call.getKeyword().accept(this, memory.getUpdated(call.getKeyword()));
    }

    @Override
    public void visit(Assignment assignment, VisitorMemory memory) {
        if(assignment.getExpression() != null && assignment.getExpression().getKeyword() != null){
            assignment.getExpression().getKeyword().accept(this, memory);
        }
    }

    @Override
    public void visit(ForLoop forLoop, VisitorMemory memory) {
        VisitorUtils.traverseForLoopSteps(this, forLoop, memory);
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
