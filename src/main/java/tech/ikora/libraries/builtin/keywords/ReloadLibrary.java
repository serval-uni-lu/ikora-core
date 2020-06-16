package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.StringType;

import java.util.Collections;

public class ReloadLibrary extends LibraryKeyword {
    public ReloadLibrary(){
        super(Type.UNKNOWN, Collections.singletonList(new StringType("name_or_instance")));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
