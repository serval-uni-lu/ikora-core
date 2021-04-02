package lu.uni.serval.ikora.core.analytics.visitor;

import lu.uni.serval.ikora.core.model.*;

public class DependencyVisitor implements NodeVisitor {
    @Override
    public void visit(SourceNode sourceNode, VisitorMemory memory) {
        VisitorUtils.accept(this, sourceNode, memory);
    }

    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, testCase, memory);
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, keyword, memory);
    }

    @Override
    public void visit(Step step, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, step, memory);
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, call, memory);
    }

    @Override
    public void visit(Assignment assignment, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, assignment, memory);
    }

    @Override
    public void visit(ForLoop forLoop, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, forLoop, memory);
    }

    @Override
    public void visit(LibraryKeyword keyword, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, keyword, memory);
    }

    @Override
    public void visit(ScalarVariable scalar, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, scalar, memory);
    }

    @Override
    public void visit(DictionaryVariable dictionary, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, dictionary, memory);
    }

    @Override
    public void visit(ListVariable list, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, list, memory);
    }

    @Override
    public void visit(TimeOut timeOut, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, timeOut, memory);
    }

    @Override
    public void visit(Argument argument, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, argument, memory);
    }

    @Override
    public <T extends SourceNode> void visit(SourceNodeTable<T> nodeTable, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, nodeTable, memory);
    }

    @Override
    public void visit(VariableAssignment variableAssignment, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, variableAssignment, memory);
    }

    @Override
    public void visit(LibraryVariable libraryVariable, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, libraryVariable, memory);
    }
}
