package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.BooleanType;
import lu.uni.serval.ikora.types.StringType;

public class SetTestDocumentation extends LibraryKeyword {
    public SetTestDocumentation(){
        super(Type.SET,
                new StringType("documentation"),
                new BooleanType("False")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
