package tech.ikora.analytics.visitor;

import tech.ikora.model.*;

public interface NodeVisitor {
    void visit(Node node, VisitorMemory memory);
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
    <T extends Node> void visit(NodeTable<T> ts, VisitorMemory memory);
}
