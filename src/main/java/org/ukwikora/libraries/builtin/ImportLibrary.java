package org.ukwikora.libraries.builtin;

import org.ukwikora.error.Error;
import org.ukwikora.error.SymbolError;
import org.ukwikora.error.SyntaxError;
import org.ukwikora.model.*;
import org.ukwikora.runner.Runtime;

import java.util.List;

public class ImportLibrary extends LibraryKeyword implements ScopeModifier {
    @Override
    public void run(Runtime runtime) {

    }

    @Override
    public void addToScope(Runtime runtime, KeywordCall call, List<Error> errors) {
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
                SymbolError error = new SymbolError("Failed to resolve dynamic import",
                        call.getFile().getFile(),
                        call.getLineRange());

                errors.add(error);

                break;
            }
        }

        if(parent != null){
            runtime.addDynamicLibrary(parent, call.getParameters());
        }
    }
}
