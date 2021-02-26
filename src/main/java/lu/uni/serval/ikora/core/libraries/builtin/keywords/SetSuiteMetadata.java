package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.BooleanType;
import lu.uni.serval.ikora.core.types.StringType;

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
