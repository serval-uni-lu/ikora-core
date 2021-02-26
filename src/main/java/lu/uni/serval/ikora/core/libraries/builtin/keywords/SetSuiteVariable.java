package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.analytics.visitor.FindSuiteVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.ListType;
import lu.uni.serval.ikora.core.types.StringType;

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
                    call.getSource(),
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
                        call.getSource(),
                        "Failed to update suite scope: malformed variable.",
                        call.getRange()
                );
            }
        }
    }
}
