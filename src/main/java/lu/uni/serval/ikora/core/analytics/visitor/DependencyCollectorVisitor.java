package lu.uni.serval.ikora.core.analytics.visitor;

import lu.uni.serval.ikora.core.model.KeywordDefinition;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.core.model.UserKeyword;

import java.util.HashSet;
import java.util.Set;

public class DependencyCollectorVisitor extends DependencyVisitor {
    private final Set<KeywordDefinition> dependencies;

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
