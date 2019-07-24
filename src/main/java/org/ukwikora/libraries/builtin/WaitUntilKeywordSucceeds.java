package org.ukwikora.libraries.builtin;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.runner.Runtime;
import org.ukwikora.model.Value;

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
    public void execute(Runtime runtime) {

    }

    @Override
    public int[] getKeywordsLaunchedPosition() {
        return new int[]{2};
    }
}
