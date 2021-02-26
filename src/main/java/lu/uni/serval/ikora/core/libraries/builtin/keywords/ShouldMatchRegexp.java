package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.BooleanType;
import lu.uni.serval.ikora.core.types.StringType;

public class ShouldMatchRegexp extends LibraryKeyword {
    public ShouldMatchRegexp(){
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
