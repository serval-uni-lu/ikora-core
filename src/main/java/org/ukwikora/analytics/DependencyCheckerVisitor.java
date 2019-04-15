package org.ukwikora.analytics;

import org.ukwikora.model.*;

import java.util.HashSet;
import java.util.Set;

public class DependencyCheckerVisitor implements StatementVisitor {
    private final Set<KeywordDefinition> keywords;
    private Set<KeywordDefinition> dependencies;

    public DependencyCheckerVisitor(Set<KeywordDefinition> keywords){
        this.keywords = keywords;
        this.dependencies = new HashSet<>();
    }

    public Set<KeywordDefinition> getDependencies() {
        return dependencies;
    }

    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        if(keywords.contains(testCase)){
            dependencies.add(testCase);
        }
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        if(keywords.contains(keyword)){
            dependencies.add(keyword);
        }

        VisitorUtils.traverseDependencies(this, keyword, memory);
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
}
