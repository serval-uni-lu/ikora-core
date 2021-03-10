package lu.uni.serval.ikora.core.utils;

import lu.uni.serval.ikora.core.model.ScopeNode;
import lu.uni.serval.ikora.core.model.SourceNode;

import java.util.Optional;

public class Ast {
    private Ast() {}

    public static <T> Optional<T> getParentByType(SourceNode node, Class<T> parentType){
        T parent = null;
        SourceNode current = node.getAstParent();

        while(current != null){
            if(parentType.isAssignableFrom(current.getClass())){
                parent = (T) current;
                break;
            }

            current = current.getAstParent();
        }

        return Optional.ofNullable(parent);
    }

    public static <T> Optional<T> getParentByType(ScopeNode node, Class<T> parentType){
        if(SourceNode.class.isAssignableFrom(node.getClass())){
            return getParentByType((SourceNode)node, parentType);
        }

        return Optional.empty();
    }


}
