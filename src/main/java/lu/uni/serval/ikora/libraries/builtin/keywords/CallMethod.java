package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.ListType;
import lu.uni.serval.ikora.types.ObjectType;
import lu.uni.serval.ikora.types.StringType;

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
