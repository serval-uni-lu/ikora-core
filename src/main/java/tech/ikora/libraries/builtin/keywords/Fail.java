package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ListType;
import tech.ikora.types.StringType;

import java.util.Arrays;

public class Fail extends LibraryKeyword {
    public Fail(){
        super(Type.ERROR, Arrays.asList(
                new StringType("message", "None"),
                new ListType("tags", "None")
        ));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
