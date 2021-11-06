package lu.uni.serval.ikora.core.utils;

import lu.uni.serval.ikora.core.analytics.KeywordStatistics;
import lu.uni.serval.ikora.core.model.KeywordDefinition;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.core.model.Token;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Cfg {
    private Cfg() {}

    public static boolean isCalledBy(SourceNode node, KeywordDefinition caller){
        if(node == caller){
            return true;
        }

        final Set<KeywordDefinition> dependencies = new HashSet<>();
        final KeywordDefinition parent = getParentKeywordDefinition(node);

        if(parent != null){
            dependencies.addAll(KeywordStatistics.getDependencies(parent));
        }
        else{
            dependencies.addAll(KeywordStatistics.getDependencies(node));
        }

        return dependencies.contains(caller);
    }

    public static Optional<KeywordDefinition> getCallerByName(SourceNode node, Token name){
        KeywordDefinition parent = getParentKeywordDefinition(node);

        if(parent == null){
            return Optional.empty();
        }

        return KeywordStatistics.getDependencies(parent).stream()
                .filter(k -> k.matches(name))
                .findFirst();
    }

    private static KeywordDefinition getParentKeywordDefinition(SourceNode node) {
        KeywordDefinition parent = null;

        if (KeywordDefinition.class.isAssignableFrom(node.getClass())) {
            parent = (KeywordDefinition) node;
        } else {
            final Optional<KeywordDefinition> optionalParent = Ast.getParentByType(node, KeywordDefinition.class);
            if (optionalParent.isPresent()) {
                parent = optionalParent.get();
            }
        }
        return parent;
    }

}
