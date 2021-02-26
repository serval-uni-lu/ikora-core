package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.BooleanType;
import lu.uni.serval.ikora.core.types.StringType;

public class GetLibraryInstance extends LibraryKeyword {
    public GetLibraryInstance(){
        super(Type.GET,
                new StringType("name", "None"),
                new BooleanType("all", "False")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
