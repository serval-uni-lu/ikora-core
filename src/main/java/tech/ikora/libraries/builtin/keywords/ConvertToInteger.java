package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.NumberType;
import tech.ikora.types.StringType;

public class ConvertToInteger extends LibraryKeyword {
    public ConvertToInteger(){
        super(Type.UNKNOWN,
                new StringType("item"),
                new NumberType("base", "None")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
