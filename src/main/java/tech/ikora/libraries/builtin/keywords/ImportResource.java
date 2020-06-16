package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.PathType;

import java.util.Collections;

public class ImportResource extends LibraryKeyword {
    public ImportResource(){
        super(Type.UNKNOWN, Collections.singletonList(new PathType("path")));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
