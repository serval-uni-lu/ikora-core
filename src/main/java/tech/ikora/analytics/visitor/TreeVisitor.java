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
    public void visit(Step step, VisitorMemory memory) {
        if(step instanceof KeywordCall){
            VisitorUtils.traverseKeywordCall(this, (KeywordCall)step, memory);
        }
        else if(step instanceof Assignment){
            VisitorUtils.traverseAssignmentCall(this, (Assignment)step, memory);
        }
        else if(step instanceof ForLoop){
            VisitorUtils.traverseForLoopSteps(this, (ForLoop)step, memory);
        }
        else{
            throw new NotImplementedException(String.format("Visitor does not support class %s", step.getClass()));
        }
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
        //leaf node
    }

    @Override
    public void visit(ScalarVariable scalar, VisitorMemory memory) {
        //leaf node
    }

    @Override
    public void visit(DictionaryVariable dictionary, VisitorMemory memory) {
        //leaf node
    }

    @Override
    public void visit(ListVariable list, VisitorMemory memory) {
        //leaf node
    }

    @Override
    public void visit(TimeOut timeOut, VisitorMemory memory) {
        //leaf node
    }

    @Override
    public void visit(Argument argument, VisitorMemory memory) {
        VisitorUtils.traverseArgument(this, argument, memory);
    }

    @Override
    public <T extends Node> void visit(NodeTable<T> ts, VisitorMemory memory) {
        throw new NotImplementedException("Cannot use a tree visitor to traverse a node table yet");
    }

    @Override
    public void visit(VariableAssignment variableAssignment, VisitorMemory memory) {
        VisitorUtils.traverseValues(this, variableAssignment, memory);
    }
}
