package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.StringType;

import java.util.Arrays;

public class GetVariableValue extends LibraryKeyword {
    public GetVariableValue(){
        super(Type.UNKNOWN, Arrays.asList(
                new StringType("name"),
                new StringType("default", "None")
        ));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
