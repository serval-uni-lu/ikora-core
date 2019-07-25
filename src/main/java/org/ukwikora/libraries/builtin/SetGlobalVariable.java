package org.ukwikora.libraries.builtin;

import org.ukwikora.model.*;
import org.ukwikora.runner.Runtime;

import java.util.Optional;

public class SetGlobalVariable extends LibraryKeyword implements ScopeModifier {
    public SetGlobalVariable(){
        this.type = Type.Set;
    }

    @Override
    public void execute(Runtime runtime) {

    }

    @Override
    public void addToScope(Runtime runtime, KeywordCall call) throws Exception {
        Optional<Value> parameter = call.getParameter(0, false);

        if(!parameter.isPresent()){
            throw new Exception(String.format("No argument for '%s': failed to update global scope", call.toString()));
        }

        runtime.addToGlobalScope(Variable.create(parameter.get()));
    }
}
