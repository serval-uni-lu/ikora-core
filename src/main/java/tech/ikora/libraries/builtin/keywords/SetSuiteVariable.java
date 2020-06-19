package tech.ikora.libraries.builtin.keywords;

import tech.ikora.analytics.visitor.FindSuiteVisitor;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.*;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ListType;
import tech.ikora.types.StringType;

import java.util.List;

public class SetSuiteVariable extends LibraryKeyword implements ScopeModifier {
    public SetSuiteVariable(){
        super(Type.SET,
                new StringType("name"),
                new ListType("values")
        );
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
                Variable variable = Variable.create(argumentList.get(0).getNameToken());

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
}
