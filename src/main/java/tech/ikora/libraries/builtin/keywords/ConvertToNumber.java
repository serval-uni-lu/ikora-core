package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.NumberType;
import tech.ikora.types.StringType;

public class ConvertToNumber extends LibraryKeyword {
    public ConvertToNumber(){
        super(Type.SET,
                new StringType("item"),
                new NumberType("precision", "None")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
