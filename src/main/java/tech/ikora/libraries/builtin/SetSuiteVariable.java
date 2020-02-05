package tech.ikora.libraries.builtin;

import tech.ikora.analytics.visitor.FindSuiteVisitor;
import tech.ikora.analytics.PathMemory;
import tech.ikora.model.*;
import tech.ikora.runner.Runtime;

import java.util.List;

public class SetSuiteVariable extends LibraryKeyword implements ScopeModifier {
    public SetSuiteVariable(){
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
                    "Failed to update suite scope: no argument found.",
                    call.getRange()
            );
        }
        else{
            try {
                Variable variable = Variable.create(argumentList.get(0).getName());

                for(int i = 1; i < argumentList.size(); ++i){
                    variable.addArgument(argumentList.get(i));
                }

                FindSuiteVisitor visitor = new FindSuiteVisitor();
                call.accept(visitor, new PathMemory());

                for(String suite: visitor.getSuites()){
                    runtime.addToSuiteScope(suite, variable);
                }
            } catch (Exception e) {
                runtime.getErrors().registerInternalError(
                        call.getFile(),
                        "Failed to update suite scope: malformed variable.",
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
