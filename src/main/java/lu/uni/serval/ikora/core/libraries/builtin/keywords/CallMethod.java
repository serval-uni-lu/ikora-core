package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.ListType;
import lu.uni.serval.ikora.core.types.ObjectType;
import lu.uni.serval.ikora.core.types.StringType;

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
