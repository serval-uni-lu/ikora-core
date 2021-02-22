package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.BooleanType;
import lu.uni.serval.ikora.types.StringType;

public class SetSuiteMetadata extends LibraryKeyword {
    public SetSuiteMetadata(){
        super(Type.SET,
                new StringType("name"),
                new StringType("value"),
                new BooleanType("append", "False"),
                new BooleanType("top", "False")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
