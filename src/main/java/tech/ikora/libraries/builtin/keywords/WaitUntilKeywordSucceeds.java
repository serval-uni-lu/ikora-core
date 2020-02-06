package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.Argument;
import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

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
