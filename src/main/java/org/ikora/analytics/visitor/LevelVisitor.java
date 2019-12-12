package org.ikora.analytics.visitor;

import org.ikora.analytics.LevelMemory;

import org.ikora.model.*;

public class LevelVisitor extends EmptyVisitor {
    int level = 0;

    private void updateLevel(VisitorMemory memory){
        if(memory instanceof LevelMemory){
            int currentLevel = ((LevelMemory)memory).getLevel();
            this.level = Math.max(currentLevel, this.level);
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

    public int getLevel() {
        return level;
    }
}
