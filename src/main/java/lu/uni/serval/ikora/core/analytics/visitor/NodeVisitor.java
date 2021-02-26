package lu.uni.serval.ikora.core.analytics.visitor;

import lu.uni.serval.ikora.core.model.*;

public interface NodeVisitor {
    void visit(SourceNode sourceNode, VisitorMemory memory);
    void visit(TestCase testCase, VisitorMemory memory);
    void visit(UserKeyword keyword, VisitorMemory memory);
    void visit(KeywordCall call, VisitorMemory memory);
    void visit(Assignment assignment, VisitorMemory memory);
    void visit(ForLoop forLoop, VisitorMemory memory);
    void visit(LibraryKeyword keyword, VisitorMemory memory);
    void visit(ScalarVariable scalar, VisitorMemory memory);
    void visit(DictionaryVariable dictionary, VisitorMemory memory);
    void visit(ListVariable list, VisitorMemory memory);
    void visit(TimeOut timeOut, VisitorMemory memory);
    void visit(Argument argument, VisitorMemory memory);
    void visit(Step step, VisitorMemory memory);
    void visit(VariableAssignment variableAssignment, VisitorMemory memory);
    void visit(LibraryVariable variable, VisitorMemory memory);
    <T extends SourceNode> void visit(SourceNodeTable<T> ts, VisitorMemory memory);
}
