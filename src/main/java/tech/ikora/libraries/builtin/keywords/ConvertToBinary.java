package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.BaseType;
import tech.ikora.types.NumberType;
import tech.ikora.types.StringType;

import java.util.Arrays;
import java.util.List;

public class ConvertToBinary extends LibraryKeyword {
    public ConvertToBinary() {
        super(Type.UNKNOWN, Arrays.asList(
                new StringType("item"),
                new NumberType("base", "None"),
                new StringType("prefix", "None"),
                new NumberType("length", "None")
        ));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
