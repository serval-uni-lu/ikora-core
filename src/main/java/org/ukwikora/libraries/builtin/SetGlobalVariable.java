package org.ukwikora.libraries.builtin;

import org.ukwikora.error.ErrorManager;
import org.ukwikora.model.*;
import org.ukwikora.runner.Runtime;

import java.util.Optional;

public class SetGlobalVariable extends LibraryKeyword implements ScopeModifier {
    public SetGlobalVariable(){
        this.type = Type.Set;
    }

    @Override
    public void run(Runtime runtime) {

    }

    @Override
    public void addToScope(Runtime runtime, KeywordCall call, ErrorManager errors) {
        Optional<Value> parameter = call.getParameter(0, false);

        if(!parameter.isPresent()){
            errors.registerInternalError(
                    "Failed to update global scope: no argument found.",
                    call.getFile().getFile(),
                    call.getLineRange()
            );
        }
        else{
            try {
                runtime.addToGlobalScope(Variable.create(parameter.get()));
            } catch (Exception e) {
                errors.registerInternalError(
                        "Failed to update global scope: malformed variable.",
                        call.getFile().getFile(),
                        call.getLineRange()
                );
            }
        }
    }
}
