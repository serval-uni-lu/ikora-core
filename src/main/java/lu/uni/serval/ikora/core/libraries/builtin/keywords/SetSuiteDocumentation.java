package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.BooleanType;
import lu.uni.serval.ikora.core.types.StringType;

public class SetSuiteDocumentation extends LibraryKeyword {
    public SetSuiteDocumentation(){
        super(Type.SET,
                new StringType("documentation"),
                new BooleanType("append", "False"),
                new BooleanType("top", "False")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
