package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.BooleanType;
import lu.uni.serval.ikora.types.StringType;

public class ShouldNotStartWith extends LibraryKeyword {
    public ShouldNotStartWith(){
        super(Type.ASSERTION,
                new StringType("string"),
                new StringType("prefix"),
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
