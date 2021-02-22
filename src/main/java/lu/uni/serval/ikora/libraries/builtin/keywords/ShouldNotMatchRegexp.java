package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.BooleanType;
import lu.uni.serval.ikora.types.StringType;

public class ShouldNotMatchRegexp extends LibraryKeyword {
    public ShouldNotMatchRegexp(){
        super(Type.ASSERTION,
                new StringType("string"),
                new StringType("pattern"),
                new StringType("message", "None"),
                new BooleanType("values", "True")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
