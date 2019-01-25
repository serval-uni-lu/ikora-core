package org.ukwikora.model;

public interface StatementVisitor {
    void visit(TestCase testCase);
    void visit(UserKeyword keyword);
    void visit(KeywordCall call);
    void visit(Assignment assignment);
    void visit(ForLoop forLoop);
    void visit(LibraryKeyword keyword);
    void visit(ScalarVariable scalar);
    void visit(DictionaryVariable dictionary);
    void visit(ListVariable list);

    boolean isAcceptable(Statement statement);
}
