package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.KeywordCall;
import lu.uni.serval.ikora.core.model.KeywordDefinition;
import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.model.ScopeModifier;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.ListType;
import lu.uni.serval.ikora.core.types.StringType;
import lu.uni.serval.ikora.core.utils.Ast;

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
