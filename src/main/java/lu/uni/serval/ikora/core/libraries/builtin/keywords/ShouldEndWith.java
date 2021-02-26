package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.BooleanType;
import lu.uni.serval.ikora.core.types.StringType;

public class ShouldEndWith extends LibraryKeyword {
    public ShouldEndWith(){
        super(Type.ASSERTION,
                new StringType("string"),
                new StringType("suffix"),
                new StringType("message", "None"),
                new BooleanType("values", "True"),
                new BooleanType("ignore_case", "False")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
