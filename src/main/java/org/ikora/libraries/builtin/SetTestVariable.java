package org.ikora.libraries.builtin;

import org.ikora.analytics.visitor.FindTestCaseVisitor;
import org.ikora.analytics.PathMemory;
import org.ikora.model.*;
import org.ikora.runner.Runtime;

import java.util.List;

public class SetTestVariable extends LibraryKeyword implements ScopeModifier {
    public SetTestVariable(){
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
                    "Failed to update test scope: no argument found.",
                    call.getPosition()
            );
        }
        else{
            try {
                Variable variable = Variable.create(argumentList.get(0).getNameAsValue());

                for(int i = 1; i < argumentList.size(); ++i){
                    variable.addElement(argumentList.get(i).getName());
                }

                FindTestCaseVisitor visitor = new FindTestCaseVisitor();
                call.accept(visitor, new PathMemory());

                for(TestCase testCase: visitor.getTestCases()){
                    runtime.addToTestScope(testCase, variable);
                }
            } catch (Exception e) {
                runtime.getErrors().registerInternalError(
                        call.getFile(),
                        "Failed to update test scope: malformed variable.",
                        call.getPosition()
                );
            }
        }
    }
}
