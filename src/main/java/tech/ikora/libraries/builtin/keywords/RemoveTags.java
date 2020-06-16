package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ListType;
import tech.ikora.types.StringType;

import java.util.Collections;

public class RemoveTags extends LibraryKeyword {
    public RemoveTags(){
        super(Type.UNKNOWN, Collections.singletonList(new ListType("tags")));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
