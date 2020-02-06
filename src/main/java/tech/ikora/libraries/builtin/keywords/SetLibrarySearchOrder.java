package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class SetLibrarySearchOrder extends LibraryKeyword {
    public SetLibrarySearchOrder(){
        this.type = Type.SET;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
