package org.ukwikora.analytics;

import org.ukwikora.model.*;

public class LevelVisitor implements NodeVisitor {
    int level = 0;

    private void updateLevel(VisitorMemory memory){
        if(memory instanceof LevelMemory){
            int level = ((LevelMemory)memory).getLevel();
            this.level = Math.max(level, this.level);
        }
    }

    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        updateLevel(memory);
        VisitorUtils.traverseSteps(this, testCase, memory);
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        updateLevel(memory);
        VisitorUtils.traverseSteps(this, keyword, memory);
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        updateLevel(memory);
        VisitorUtils.traverseKeywordCall(this, call, memory);
    }

    @Override
    public void visit(Assignment assignment, VisitorMemory memory) {
        updateLevel(memory);
        VisitorUtils.traverseAssignmentCall(this, assignment, memory);
    }

    @Override
    public void visit(ForLoop forLoop, VisitorMemory memory) {
        updateLevel(memory);
        VisitorUtils.traverseForLoopSteps(this, forLoop, memory);
    }

    @Override
    public void visit(LibraryKeyword keyword, VisitorMemory memory) {
        updateLevel(memory);
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

    @Override
    public void visit(TimeOut timeOut, VisitorMemory memory) {

    }

    public int getLevel() {
        return level;
    }
}
