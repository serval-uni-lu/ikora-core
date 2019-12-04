package org.ukwikora.libraries.builtin;

import org.ukwikora.analytics.FindTestCaseVisitor;
import org.ukwikora.analytics.PathMemory;
import org.ukwikora.error.ErrorManager;
import org.ukwikora.error.InternalError;
import org.ukwikora.model.*;
import org.ukwikora.runner.Runtime;

import java.util.Optional;

public class SetTestVariable extends LibraryKeyword implements ScopeModifier {
    public SetTestVariable(){
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
                    "Failed to update test scope: no argument found.",
                    call.getFile().getFile(),
                    call.getLineRange());
        }
        else{
            try {
                Variable variable = Variable.create(parameter.get());

                FindTestCaseVisitor visitor = new FindTestCaseVisitor();
                call.accept(visitor, new PathMemory());

                for(TestCase testCase: visitor.getTestCases()){
                    runtime.addToTestScope(testCase, variable);
                }
            } catch (Exception e) {
                errors.registerInternalError(
                        "Failed to update test scope: malformed variable.",
                        call.getFile().getFile(),
                        call.getLineRange()
                );
            }
        }
    }
}
