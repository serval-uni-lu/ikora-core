package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.ListType;
import lu.uni.serval.ikora.types.PathType;

public class ImportVariables extends LibraryKeyword {
    public ImportVariables(){
        super(Type.CONFIGURATION,
                new PathType("path"),
                new ListType("args")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
