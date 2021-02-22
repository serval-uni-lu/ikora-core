package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.ConditionType;
import lu.uni.serval.ikora.types.ListType;

public class ReturnFromKeywordIf extends LibraryKeyword {
    public ReturnFromKeywordIf(){
        super(Type.CONTROL_FLOW,
                new ConditionType("condition"),
                new ListType("return_values")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
