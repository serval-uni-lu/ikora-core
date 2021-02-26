package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.DictionaryType;
import lu.uni.serval.ikora.core.types.ListType;
import lu.uni.serval.ikora.core.types.ObjectType;

public class ShouldNotContainAny extends LibraryKeyword {
    public ShouldNotContainAny(){
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
