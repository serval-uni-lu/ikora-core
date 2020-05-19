package tech.ikora.analytics.visitor;

import tech.ikora.model.*;

public class SizeVisitor extends EmptyVisitor {
    public class Result{
        private int totalSize = 0;
        private int testCaseSize = 0;
        private int userKeywordSize = 0;
        private int libraryKeywordSize = 0;

        public int getTotalSize() {
            return totalSize;
        }

        public int getTestCaseSize() {
            return testCaseSize;
        }

        public int getUserKeywordSize() {
            return userKeywordSize;
        }

        public int getLibraryKeywordSize() {
            return libraryKeywordSize;
        }
    }

    private Result result = new Result();

    public Result getResult() {
        return result;
    }

    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        ++result.totalSize;
        ++result.testCaseSize;
        VisitorUtils.traverseSteps(this, testCase, memory);
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        ++result.totalSize;
        ++result.userKeywordSize;
        VisitorUtils.traverseSteps(this, keyword, memory);
    }

    @Override
    public void visit(LibraryKeyword keyword, VisitorMemory memory) {
        ++result.totalSize;
        ++result.libraryKeywordSize;
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
