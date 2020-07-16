package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.*;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ListType;
import tech.ikora.types.StringType;
import tech.ikora.utils.Ast;

import java.util.Optional;

public class ImportLibrary extends LibraryKeyword implements ScopeModifier {
    public ImportLibrary(){
        super(Type.CONFIGURATION,
                new StringType("name"),
                new ListType("args")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addToScope(Runtime runtime, KeywordCall call) {
        Optional<KeywordDefinition> parent = Ast.getParentByType(call, KeywordDefinition.class);

        if(parent.isPresent()){
            runtime.addDynamicLibrary(parent.get(), call.getArgumentList());
        }
        else{
            runtime.getErrors().registerSymbolError(
                    call.getSource(),
                    "Failed to resolve dynamic import",
                    call.getRange()
            );
        }
    }
}
