package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.PathType;


public class ImportResource extends LibraryKeyword {
    public ImportResource(){
        super(Type.CONFIGURATION, new PathType("path"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
