package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.ListType;
import lu.uni.serval.ikora.core.types.StringType;

public class Fail extends LibraryKeyword {
    public Fail(){
        super(Type.ERROR,
                new StringType("message", "None"),
                new ListType("tags", "None")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
