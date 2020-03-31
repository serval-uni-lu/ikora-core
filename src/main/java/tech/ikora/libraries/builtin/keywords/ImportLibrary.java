package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.*;
import tech.ikora.runner.Runtime;

public class ImportLibrary extends LibraryKeyword implements ScopeModifier {
    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addToScope(Runtime runtime, KeywordCall call) {
        KeywordDefinition parent = null;
        SourceNode current = call.getAstParent();

        while(current != null){
            if(Step.class.isAssignableFrom(current.getClass())) {
                current = current.getAstParent();
            }
            else if(KeywordDefinition.class.isAssignableFrom(current.getClass())){
                parent = (KeywordDefinition) current;
                break;
            }
            else{
                runtime.getErrors().registerSymbolError(
                        call.getFile(),
                        "Failed to resolve dynamic import",
                        call.getRange()
                );

                break;
            }
        }

        if(parent != null){
            runtime.addDynamicLibrary(parent, call.getArgumentList());
        }
    }
}
