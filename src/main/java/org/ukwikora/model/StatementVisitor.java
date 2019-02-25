package org.ukwikora.model;

import org.ukwikora.analytics.VisitorMemory;

public interface StatementVisitor {
    void visit(TestCase testCase, VisitorMemory memory);
    void visit(UserKeyword keyword, VisitorMemory memory);
    void visit(KeywordCall call, VisitorMemory memory);
    void visit(Assignment assignment, VisitorMemory memory);
    void visit(ForLoop forLoop, VisitorMemory memory);
    void visit(LibraryKeyword keyword, VisitorMemory memory);
    void visit(ScalarVariable scalar, VisitorMemory memory);
    void visit(DictionaryVariable dictionary, VisitorMemory memory);
    void visit(ListVariable list, VisitorMemory memory);
}
