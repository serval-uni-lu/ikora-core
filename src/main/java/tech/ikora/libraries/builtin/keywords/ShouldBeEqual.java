package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.BooleanType;
import tech.ikora.types.StringType;

public class ShouldBeEqual extends LibraryKeyword {
    public ShouldBeEqual(){
        super(Type.ASSERTION,
                new StringType("first"),
                new StringType("second"),
                new StringType("message", "None"),
                new BooleanType("values", "True"),
                new BooleanType("ignore_case", "False"),
                new StringType("formatter", "str")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
