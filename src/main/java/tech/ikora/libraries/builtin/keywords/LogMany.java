package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ListType;

import java.util.Collections;

public class LogMany extends LibraryKeyword {
    public LogMany(){
        super(Type.LOG, Collections.singletonList(new ListType("messages")));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
