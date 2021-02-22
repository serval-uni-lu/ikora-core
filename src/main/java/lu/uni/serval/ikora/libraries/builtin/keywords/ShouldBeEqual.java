package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.BooleanType;
import lu.uni.serval.ikora.types.StringType;

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
