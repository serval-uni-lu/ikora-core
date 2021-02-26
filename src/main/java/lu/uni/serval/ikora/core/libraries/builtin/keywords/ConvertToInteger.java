package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.NumberType;
import lu.uni.serval.ikora.core.types.StringType;

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
