package org.ukwikora.libraries.builtin;

import org.ukwikora.analytics.FindSuiteVisitor;
import org.ukwikora.analytics.PathMemory;
import org.ukwikora.model.*;
import org.ukwikora.runner.Runtime;

import java.util.Optional;

public class SetSuiteVariable extends LibraryKeyword implements ScopeModifier {
    public SetSuiteVariable(){
        this.type = Type.Set;
    }

    @Override
    public void run(Runtime runtime) {

    }

    @Override
    public void addToScope(Runtime runtime, KeywordCall call) throws Exception {
        Optional<Value> parameter = call.getParameter(0, false);

        if(!parameter.isPresent()){
            throw new Exception(String.format("No argument for '%s': failed to update suite scope", call.toString()));
        }

        Variable variable = Variable.create(parameter.get());

        FindSuiteVisitor visitor = new FindSuiteVisitor();
        call.accept(visitor, new PathMemory());

        for(String suite: visitor.getSuites()){
            runtime.addToSuiteScope(suite, variable);
        }
    }
}
