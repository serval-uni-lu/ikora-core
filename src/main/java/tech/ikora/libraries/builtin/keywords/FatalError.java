package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.StringType;

import java.util.Collections;

public class FatalError extends LibraryKeyword {
    public FatalError(){
        super(Type.ERROR, Collections.singletonList(new StringType("message", "None")));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
