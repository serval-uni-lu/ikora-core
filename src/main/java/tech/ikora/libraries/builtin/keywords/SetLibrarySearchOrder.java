package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ListType;

public class SetLibrarySearchOrder extends LibraryKeyword {
    public SetLibrarySearchOrder(){
        super(Type.SET, new ListType("search_order"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
