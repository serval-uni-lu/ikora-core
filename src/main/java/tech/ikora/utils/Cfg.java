package tech.ikora.utils;

import tech.ikora.analytics.KeywordStatistics;
import tech.ikora.model.KeywordDefinition;
import tech.ikora.model.SourceNode;

import java.util.Optional;
import java.util.Set;

public class Cfg {
    public static boolean isCalledBy(SourceNode node, KeywordDefinition caller){
        if(node == caller){
            return true;
        }

        KeywordDefinition parent = null;

        if(KeywordDefinition.class.isAssignableFrom(node.getClass())){
            parent = (KeywordDefinition)node;
        }
        else{
            final Optional<KeywordDefinition> optionalParent = Ast.getParentByType(node, KeywordDefinition.class);
            if(optionalParent.isPresent()){
                parent = optionalParent.get();
            }
        }

        if(parent == null){
            return false;
        }

        final Set<KeywordDefinition> dependencies = KeywordStatistics.getDependencies(parent);

        return dependencies.contains(caller);
    }
}
