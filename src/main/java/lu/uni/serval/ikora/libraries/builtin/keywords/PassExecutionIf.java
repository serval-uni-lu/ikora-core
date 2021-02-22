package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.ListType;
import lu.uni.serval.ikora.types.StringType;

public class PassExecutionIf extends LibraryKeyword {
    public PassExecutionIf(){
        super(Type.CONTROL_FLOW,
                new StringType("message"),
                new ListType("tags")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
