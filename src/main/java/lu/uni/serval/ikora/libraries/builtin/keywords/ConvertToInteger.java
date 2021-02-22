package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.NumberType;
import lu.uni.serval.ikora.types.StringType;

public class ConvertToInteger extends LibraryKeyword {
    public ConvertToInteger(){
        super(Type.SET,
                new StringType("item"),
                new NumberType("base", "None")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
