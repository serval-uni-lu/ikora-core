package org.ukwikora.analytics;

import org.ukwikora.model.*;
import org.ukwikora.utils.VisitorUtils;

import java.util.HashSet;
import java.util.Set;

public class SizeVisitor implements StatementVisitor {
    private int size;
    private Set<Statement> memory;

    public SizeVisitor(){
        size = 0;
        memory = new HashSet<>();
    }

    public int getSize(){
        return size;
    }

    @Override
    public void visit(TestCase testCase) {
        ++size;
        VisitorUtils.traverseSteps(this, testCase);
    }

    @Override
    public void visit(UserKeyword keyword) {
        ++size;
        VisitorUtils.traverseSteps(this, keyword);
    }

    @Override
    public void visit(LibraryKeyword keyword) {
        ++size;
    }

    @Override
    public void visit(KeywordCall call) {
        if(call.getKeyword() == null){
            return;
        }

        if(call.hasKeywordParameters()){
            for(Step step: call.getKeywordParameter()){
                if(step == null){
                    continue;
                }

                step.accept(this);
            }
        }

        call.getKeyword().accept(this);
    }

    @Override
    public void visit(Assignment assignment) {
        if(assignment.getExpression() != null && assignment.getExpression().getKeyword() != null){
            assignment.getExpression().getKeyword().accept(this);
        }
    }

    @Override
    public void visit(ForLoop forLoop) {
        VisitorUtils.traverseForLoopSteps(this, forLoop);
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
        return memory.add(statement);
    }
}
