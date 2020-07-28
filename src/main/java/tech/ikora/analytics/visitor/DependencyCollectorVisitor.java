package tech.ikora.analytics.visitor;

import tech.ikora.model.KeywordDefinition;
import tech.ikora.model.TestCase;
import tech.ikora.model.UserKeyword;

import java.util.HashSet;
import java.util.Set;

public class DependencyCollectorVisitor extends DependencyVisitor {
    private Set<KeywordDefinition> dependencies;

    public DependencyCollectorVisitor(){
        this.dependencies = new HashSet<>();
    }

    public Set<KeywordDefinition> getDependencies() {
        return dependencies;
    }

    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        dependencies.add(testCase);
        VisitorUtils.traverseDependencies(this, testCase, memory);
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        dependencies.add(keyword);
        VisitorUtils.traverseDependencies(this, keyword, memory);
    }
}
