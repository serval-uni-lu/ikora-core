package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.NumberType;
import tech.ikora.types.StringType;

import java.util.Arrays;

public class LengthShouldBe extends LibraryKeyword {
    public LengthShouldBe(){
        super(Type.ASSERTION, Arrays.asList(
                new StringType("item"),
                new NumberType("length"),
                new StringType("message", "None")
        ));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
