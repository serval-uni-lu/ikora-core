package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.BooleanType;
import lu.uni.serval.ikora.core.types.StringType;

public class ShouldBeEqual extends LibraryKeyword {
    public ShouldBeEqual(){
        super(Type.ASSERTION,
                new StringType("first"),
                new StringType("second"),
                new StringType("message", "None"),
                new BooleanType("values", "True"),
                new BooleanType("ignore_case", "False"),
                new StringType("formatter", "str")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
