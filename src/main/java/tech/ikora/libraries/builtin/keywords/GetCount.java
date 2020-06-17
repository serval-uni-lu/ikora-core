package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.StringType;

public class GetCount extends LibraryKeyword {
    public GetCount(){
        super(Type.UNKNOWN,
                new StringType("container"),
                new StringType("item")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
