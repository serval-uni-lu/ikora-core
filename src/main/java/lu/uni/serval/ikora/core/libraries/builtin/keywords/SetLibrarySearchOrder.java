package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.ListType;

public class SetLibrarySearchOrder extends LibraryKeyword {
    public SetLibrarySearchOrder(){
        super(Type.CONFIGURATION, new ListType("search_order"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
