package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ConditionType;
import tech.ikora.types.KeywordType;
import tech.ikora.types.ListType;
import tech.ikora.types.StringType;

import java.util.Arrays;

public class ReturnFromKeywordIf extends LibraryKeyword {
    public ReturnFromKeywordIf(){
        super(Type.CONTROL_FLOW, Arrays.asList(
                new ConditionType("condition"),
                new ListType("return_values")
        ));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
