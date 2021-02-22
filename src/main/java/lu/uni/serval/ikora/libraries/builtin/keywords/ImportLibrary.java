package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.KeywordCall;
import lu.uni.serval.ikora.model.KeywordDefinition;
import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.model.ScopeModifier;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.ListType;
import lu.uni.serval.ikora.types.StringType;
import lu.uni.serval.ikora.utils.Ast;

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
