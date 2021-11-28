package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.utils.Ast;
import lu.uni.serval.ikora.core.runner.Runtime;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class VariableResolver {
    private VariableResolver() {}

    public static void resolve(Variable variable, Runtime runtime){
        final Set<Dependable> definitions = new HashSet<>();

        Optional<ScopeNode> parentScope = Ast.getParentByType(variable, ScopeNode.class);
        while(parentScope.isPresent()){
            definitions.addAll(parentScope.get().findDefinition(variable));
            if(!definitions.isEmpty()) break;

            parentScope = Ast.getParentByType(parentScope.get(), ScopeNode.class);
        }

        if(definitions.isEmpty()){
            final SourceFile sourceFile = variable.getSourceFile();
            definitions.addAll(sourceFile.findVariable(variable.getDefinitionToken()));
        }

        if(definitions.isEmpty()){
            runtime.findLibraryVariable("", variable.getDefinitionToken()).ifPresent(definitions::add);
        }

        definitions.forEach(d -> variable.linkToDefinition(d, Link.Import.STATIC));
    }
}
