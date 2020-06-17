package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.StringType;

public class ReloadLibrary extends LibraryKeyword {
    public ReloadLibrary(){
        super(Type.UNKNOWN, new StringType("name_or_instance"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
