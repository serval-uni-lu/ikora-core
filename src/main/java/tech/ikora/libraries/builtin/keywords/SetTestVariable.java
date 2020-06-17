package tech.ikora.libraries.builtin.keywords;

import tech.ikora.analytics.visitor.FindTestCaseVisitor;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.*;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ListType;
import tech.ikora.types.StringType;

import java.util.List;

public class SetTestVariable extends LibraryKeyword implements ScopeModifier {
    public SetTestVariable(){
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
                    "Failed to update test scope: no argument found.",
                    call.getRange()
            );
        }
        else{
            try {
                Variable variable = Variable.create(argumentList.get(0).getNameToken());

                for(int i = 1; i < argumentList.size(); ++i){
                    variable.addValue(argumentList.get(i));
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
                        call.getRange()
                );
            }
        }
    }
}
