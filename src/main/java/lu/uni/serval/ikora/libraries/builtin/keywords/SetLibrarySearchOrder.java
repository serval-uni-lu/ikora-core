package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.ListType;

public class SetLibrarySearchOrder extends LibraryKeyword {
    public SetLibrarySearchOrder(){
        super(Type.CONFIGURATION, new ListType("search_order"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
