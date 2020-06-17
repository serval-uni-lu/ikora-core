package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.BooleanType;
import tech.ikora.types.NumberType;
import tech.ikora.types.StringType;

public class ShouldBeEqualAsIntegers extends LibraryKeyword {
    public ShouldBeEqualAsIntegers(){
        super(Type.ASSERTION,
                new NumberType("first"),
                new NumberType("second"),
                new StringType("message", "None"),
                new BooleanType("values", "None"),
                new NumberType("base", "None")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
