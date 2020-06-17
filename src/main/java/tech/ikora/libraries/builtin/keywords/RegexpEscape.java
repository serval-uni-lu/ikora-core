package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ListType;

public class RegexpEscape extends LibraryKeyword {
    public RegexpEscape(){
        super(Type.UNKNOWN, new ListType("patterns"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
