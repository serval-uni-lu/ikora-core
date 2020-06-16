package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

import java.util.Collections;

public class NoOperation extends LibraryKeyword {
    public NoOperation(){
        super(Type.UNKNOWN, Collections.emptyList());
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
