package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.BooleanType;
import tech.ikora.types.StringType;

public class ShouldMatchRegexp extends LibraryKeyword {
    public ShouldMatchRegexp(){
        super(Type.ASSERTION,
                new StringType("string"),
                new StringType("pattern"),
                new StringType("message", "None"),
                new BooleanType("values", "True")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
