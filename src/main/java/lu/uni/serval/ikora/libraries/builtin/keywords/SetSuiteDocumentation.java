package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.BooleanType;
import lu.uni.serval.ikora.types.StringType;

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
