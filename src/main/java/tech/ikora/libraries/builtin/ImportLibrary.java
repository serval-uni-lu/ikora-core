package tech.ikora.libraries.builtin;

import tech.ikora.exception.InvalidDependencyException;
import tech.ikora.model.*;
import tech.ikora.runner.Runtime;

public class ImportLibrary extends LibraryKeyword implements ScopeModifier {
    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addToScope(Runtime runtime, KeywordCall call) {
        try {
            KeywordDefinition parent = null;
            Node current = call.getParent();

            while(current != null){
                if(Step.class.isAssignableFrom(current.getClass())) {
                    current = ((Step) current).getParent();
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
        } catch (InvalidDependencyException e) {
            runtime.getErrors().registerInternalError(
                    call.getFile(),
                    e.getMessage(),
                    call.getRange()
            );
        }
    }
}
