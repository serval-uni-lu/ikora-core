package org.ukwikora.libraries.builtin;

import org.ukwikora.error.Error;
import org.ukwikora.error.InternalError;
import org.ukwikora.model.*;
import org.ukwikora.runner.Runtime;

import java.util.List;
import java.util.Optional;

public class SetGlobalVariable extends LibraryKeyword implements ScopeModifier {
    public SetGlobalVariable(){
        this.type = Type.Set;
    }

    @Override
    public void run(Runtime runtime) {

    }

    @Override
    public void addToScope(Runtime runtime, KeywordCall call, List<Error> errors) {
        Optional<Value> parameter = call.getParameter(0, false);

        if(!parameter.isPresent()){
            InternalError error = new InternalError("Failed to update global scope: no argument found.",
                    call.getFile().getFile(),
                    call.getLineRange());

            errors.add(error);
        }
        else{
            try {
                runtime.addToGlobalScope(Variable.create(parameter.get()));
            } catch (Exception e) {
                InternalError error = new InternalError("Failed to update global scope: malformed variable.",
                        call.getFile().getFile(),
                        call.getLineRange());

                errors.add(error);
            }
        }
    }
}
