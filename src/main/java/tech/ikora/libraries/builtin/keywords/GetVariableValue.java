package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.StringType;

public class GetVariableValue extends LibraryKeyword {
    public GetVariableValue(){
        super(Type.UNKNOWN,
                new StringType("name"),
                new StringType("default", "None")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
