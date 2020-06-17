package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.StringType;

public class ConvertToBoolean extends LibraryKeyword {
    public ConvertToBoolean(){
        super(Type.UNKNOWN, new StringType("item"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
