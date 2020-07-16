package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.BooleanType;
import tech.ikora.types.NumberType;
import tech.ikora.types.StringType;

public class ConvertToHex extends LibraryKeyword {
    public ConvertToHex(){
        super(Type.SET,
                new StringType("item"),
                new NumberType("base", "None"),
                new StringType("prefix", "None"),
                new BooleanType("lowercase", "False")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
