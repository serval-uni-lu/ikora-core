package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ListType;
import tech.ikora.types.ObjectType;
import tech.ikora.types.StringType;

public class CallMethod extends LibraryKeyword {
    public CallMethod(){
        super(Type.UNKNOWN,
                new ObjectType("object"),
                new StringType("method_name"),
                new ListType("kwargs")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
