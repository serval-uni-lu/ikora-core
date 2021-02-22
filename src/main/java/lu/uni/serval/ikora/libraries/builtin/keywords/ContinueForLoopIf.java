package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.ConditionType;

public class ContinueForLoopIf extends LibraryKeyword {
    public ContinueForLoopIf(){
        super(Type.CONTROL_FLOW, new ConditionType("condition"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
