package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.*;
import tech.ikora.runner.Runtime;

import java.util.List;

public class SetGlobalVariable extends LibraryKeyword implements ScopeModifier {
    public SetGlobalVariable(){
        this.type = Type.SET;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addToScope(Runtime runtime, KeywordCall call) {
        List<Argument> argumentList = call.getArgumentList();

        if(argumentList.size() < 2){
            runtime.getErrors().registerInternalError(
                    call.getFile(),
                    "Failed to update global scope: not enough arguments found. Need 2!",
                    call.getRange()
            );
        }
        else{
            try {
                Variable variable = Variable.create(argumentList.get(0).getName());

                for(int i = 1; i < argumentList.size(); ++i){
                    variable.addValue(argumentList.get(i));
                }

                runtime.addToGlobalScope(variable);
            } catch (Exception e) {
                runtime.getErrors().registerInternalError(
                        call.getFile(),
                        "Failed to update global scope: malformed variable.",
                        call.getRange()
                );
            }
        }
    }


    @Override
    public int getMaxNumberArguments() {
        return -1;
    }
}
