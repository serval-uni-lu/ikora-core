package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.BooleanType;
import lu.uni.serval.ikora.types.NumberType;
import lu.uni.serval.ikora.types.ObjectType;
import lu.uni.serval.ikora.types.StringType;

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
