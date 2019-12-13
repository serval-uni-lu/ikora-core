package org.ikora.libraries.builtin;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;
import org.ikora.model.Value;

public class WaitUntilKeywordSucceeds extends LibraryKeyword {
    public WaitUntilKeywordSucceeds(){
        this.type = Type.Synchronisation;
    }

    @Override
    public Value.Type[] getArgumentTypes() {
        return new Value.Type[]{
                Value.Type.String,
                Value.Type.String,
                Value.Type.Keyword,
                Value.Type.Kwargs
        };
    }

    @Override
    public void run(Runtime runtime) {

    }
}
