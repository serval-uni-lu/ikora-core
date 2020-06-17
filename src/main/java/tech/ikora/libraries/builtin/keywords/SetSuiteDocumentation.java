package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.BooleanType;
import tech.ikora.types.StringType;

public class SetSuiteDocumentation extends LibraryKeyword {
    public SetSuiteDocumentation(){
        super(Type.SET,
                new StringType("documentation"),
                new BooleanType("append", "False"),
                new BooleanType("top", "False")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
