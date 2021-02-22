package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.ConditionType;

public class ExitForLoopIf extends LibraryKeyword {
    public ExitForLoopIf(){
        super(Type.CONTROL_FLOW, new ConditionType("condition"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
