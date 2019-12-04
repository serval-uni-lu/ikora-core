package org.ukwikora.libraries.builtin;

import org.ukwikora.error.ErrorManager;
import org.ukwikora.model.*;
import org.ukwikora.runner.Runtime;

public class ImportLibrary extends LibraryKeyword implements ScopeModifier {
    @Override
    public void run(Runtime runtime) {

    }

    @Override
    public void addToScope(Runtime runtime, KeywordCall call, ErrorManager errors) {
        KeywordDefinition parent = null;
        Keyword current = call.getParent();

        while(current != null){
            if(current.getClass() == Step.class) {
                current = ((Step) current).getParent();
            }
            else if(current.getClass() == KeywordDefinition.class){
                parent = (KeywordDefinition) current;
                break;
            }
            else{
                errors.registerSymbolError(
                        "Failed to resolve dynamic import",
                        call.getFile().getFile(),
                        call.getLineRange()
                );

                break;
            }
        }

        if(parent != null){
            runtime.addDynamicLibrary(parent, call.getParameters());
        }
    }
}
