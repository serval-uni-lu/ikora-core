package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.DictionaryType;
import lu.uni.serval.ikora.types.ListType;
import lu.uni.serval.ikora.types.ObjectType;

public class ShouldContainAny extends LibraryKeyword {
    public ShouldContainAny(){
        super(Type.ASSERTION,
                new ObjectType("container"),
                new ListType("items"),
                new DictionaryType("configuration")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
