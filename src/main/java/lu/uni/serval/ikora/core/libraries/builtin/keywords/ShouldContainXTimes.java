package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.BooleanType;
import lu.uni.serval.ikora.core.types.NumberType;
import lu.uni.serval.ikora.core.types.ObjectType;
import lu.uni.serval.ikora.core.types.StringType;

public class ShouldContainXTimes extends LibraryKeyword {
    public ShouldContainXTimes(){
        super(Type.ASSERTION,
                new ObjectType("container"),
                new StringType("item"),
                new NumberType("count"),
                new StringType("message", "None"),
                new BooleanType("ignore_case", "False")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
