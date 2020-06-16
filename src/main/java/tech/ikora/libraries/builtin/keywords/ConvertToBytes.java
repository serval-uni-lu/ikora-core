package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.StringType;

import java.util.Arrays;

public class ConvertToBytes extends LibraryKeyword {
    public ConvertToBytes(){
        super(Type.UNKNOWN, Arrays.asList(
                new StringType("item"),
                new StringType("input_type", "text")
        ));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
