package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ListType;
import tech.ikora.types.StringType;

import java.util.Collections;

public class ReturnFromKeyword extends LibraryKeyword {
    public ReturnFromKeyword(){
        super(Type.CONTROL_FLOW, Collections.singletonList(new ListType("return_values")));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
