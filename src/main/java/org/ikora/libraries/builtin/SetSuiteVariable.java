package org.ikora.libraries.builtin;

import org.ikora.analytics.visitor.FindSuiteVisitor;
import org.ikora.analytics.PathMemory;
import org.ikora.model.*;
import org.ikora.runner.Runtime;

import java.util.List;

public class SetSuiteVariable extends LibraryKeyword implements ScopeModifier {
    public SetSuiteVariable(){
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
                    "Failed to update suite scope: no argument found.",
                    call.getPosition()
            );
        }
        else{
            try {
                Variable variable = Variable.create(argumentList.get(0).getNameAsValue());

                for(int i = 1; i < argumentList.size(); ++i){
                    variable.addElement(argumentList.get(i).getName());
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
