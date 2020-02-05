package tech.ikora.analytics.visitor;

import tech.ikora.model.KeywordDefinition;
import tech.ikora.model.TestCase;
import tech.ikora.model.UserKeyword;

import java.util.HashSet;
import java.util.Set;

public class DependencyCheckerVisitor extends DependencyVisitor {
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
}
