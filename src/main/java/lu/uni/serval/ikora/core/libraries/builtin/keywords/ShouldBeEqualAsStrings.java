package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.BooleanType;
import lu.uni.serval.ikora.core.types.NumberType;
import lu.uni.serval.ikora.core.types.StringType;

public class ShouldBeEqualAsStrings extends LibraryKeyword {
    public ShouldBeEqualAsStrings(){
       super(Type.ASSERTION,
               new NumberType("first"),
               new NumberType("second"),
               new StringType("message", "None"),
               new BooleanType("values", "None"),
               new BooleanType("ignore_case", "False"),
               new StringType("formatter", "str")
       );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
