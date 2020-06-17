package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ConditionType;

public class ExitForLoopIf extends LibraryKeyword {
    public ExitForLoopIf(){
        super(Type.CONTROL_FLOW, new ConditionType("condition"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
