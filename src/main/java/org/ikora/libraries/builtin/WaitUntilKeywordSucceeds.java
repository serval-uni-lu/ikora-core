package org.ikora.libraries.builtin;

import org.ikora.model.Argument;
import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;
import org.ikora.model.Value;

public class WaitUntilKeywordSucceeds extends LibraryKeyword {
    public WaitUntilKeywordSucceeds(){
        this.type = Type.SYNCHRONISATION;
    }

    @Override
    public Argument.Type[] getArgumentTypes() {
        return new Argument.Type[]{
                Argument.Type.STRING,
                Argument.Type.STRING,
                Argument.Type.KEYWORD,
                Argument.Type.KWARGS
        };
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
