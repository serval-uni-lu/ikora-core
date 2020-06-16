package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ListType;
import tech.ikora.types.StringType;

import java.util.Arrays;

public class PassExecutionIf extends LibraryKeyword {
    public PassExecutionIf(){
        super(Type.CONTROL_FLOW, Arrays.asList(
                new StringType("message"),
                new ListType("tags")
        ));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
