package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.NumberType;
import tech.ikora.types.StringType;

public class ConvertToBinary extends LibraryKeyword {
    public ConvertToBinary() {
        super(Type.UNKNOWN,
                new StringType("item"),
                new NumberType("base", "None"),
                new StringType("prefix", "None"),
                new NumberType("length", "None")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
