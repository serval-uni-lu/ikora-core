package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ConditionType;
import tech.ikora.types.ListType;

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
