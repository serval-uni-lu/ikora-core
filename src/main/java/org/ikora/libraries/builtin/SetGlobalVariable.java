package org.ikora.libraries.builtin;

import org.ikora.model.*;
import org.ikora.runner.Runtime;

import java.util.List;

public class SetGlobalVariable extends LibraryKeyword implements ScopeModifier {
    public SetGlobalVariable(){
        this.type = Type.Set;
    }

    @Override
    public void run(Runtime runtime) {

    }

    @Override
    public void addToScope(Runtime runtime, KeywordCall call) {
        List<Argument> argumentList = call.getArgumentList();

        if(argumentList.size() < 2){
            runtime.getErrors().registerInternalError(
                    call.getFile(),
                    "Failed to update global scope: not enough arguments found. Need 2!",
                    call.getPosition()
            );
        }
        else{
            try {
                Variable variable = Variable.create(argumentList.get(0).getNameAsValue());

                for(int i = 1; i < argumentList.size(); ++i){
                    variable.addElement(argumentList.get(i).getName());
                }

                runtime.addToGlobalScope(variable);
            } catch (Exception e) {
                runtime.getErrors().registerInternalError(
                        call.getFile(),
                        "Failed to update global scope: malformed variable.",
                        call.getPosition()
                );
            }
        }
    }


    @Override
    public int getMaxNumberArguments() {
        return -1;
    }
}
