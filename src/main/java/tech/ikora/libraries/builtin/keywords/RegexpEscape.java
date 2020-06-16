package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ListType;

import java.util.Collections;

public class RegexpEscape extends LibraryKeyword {
    public RegexpEscape(){
        super(Type.UNKNOWN, Collections.singletonList(new ListType("patterns")));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
