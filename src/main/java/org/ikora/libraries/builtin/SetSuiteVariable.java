package org.ikora.libraries.builtin;

import org.ikora.analytics.FindSuiteVisitor;
import org.ikora.analytics.PathMemory;
import org.ikora.error.ErrorManager;
import org.ikora.model.*;
import org.ikora.runner.Runtime;

import java.util.Optional;

public class SetSuiteVariable extends LibraryKeyword implements ScopeModifier {
    public SetSuiteVariable(){
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
                    "Failed to update suite scope: no argument found.",
                    call.getFile().getFile(),
                    call.getLineRange()
            );
        }
        else{
            try {
                Variable variable = Variable.create(parameter.get());

                FindSuiteVisitor visitor = new FindSuiteVisitor();
                call.accept(visitor, new PathMemory());

                for(String suite: visitor.getSuites()){
                    runtime.addToSuiteScope(suite, variable);
                }
            } catch (Exception e) {
                errors.registerInternalError(
                        "Failed to update suite scope: malformed variable.",
                        call.getFile().getFile(),
                        call.getLineRange()
                );
            }
        }
    }
}
