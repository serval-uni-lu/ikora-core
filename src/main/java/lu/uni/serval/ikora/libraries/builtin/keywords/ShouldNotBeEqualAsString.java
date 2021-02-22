package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.BooleanType;
import lu.uni.serval.ikora.types.NumberType;
import lu.uni.serval.ikora.types.StringType;

public class ShouldNotBeEqualAsString extends LibraryKeyword {
    public ShouldNotBeEqualAsString(){
        super(Type.ASSERTION,
                new NumberType("first"),
                new NumberType("second"),
                new StringType("message", "None"),
                new BooleanType("values", "None"),
                new BooleanType("ignore_case", "False")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
