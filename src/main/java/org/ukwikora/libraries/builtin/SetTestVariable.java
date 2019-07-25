package org.ukwikora.libraries.builtin;

import org.ukwikora.analytics.FindTestCaseVisitor;
import org.ukwikora.analytics.PathMemory;
import org.ukwikora.model.*;
import org.ukwikora.runner.Runtime;

import java.util.Optional;

public class SetTestVariable extends LibraryKeyword implements ScopeModifier {
    public SetTestVariable(){
        this.type = Type.Set;
    }

    @Override
    public void execute(Runtime runtime) {

    }

    @Override
    public void addToScope(Runtime runtime, KeywordCall call) throws Exception {
        Optional<Value> parameter = call.getParameter(0, false);

        if(!parameter.isPresent()){
            throw new Exception(String.format("No argument for '%s': failed to update suite scope", call.toString()));
        }

        Variable variable = Variable.create(parameter.get());

        FindTestCaseVisitor visitor = new FindTestCaseVisitor();
        call.accept(visitor, new PathMemory());

        for(TestCase testCase: visitor.getTestCases()){
            runtime.addToTestScope(testCase, variable);
        }
    }
}
