package tech.ikora.libraries.selenium.waiting;

import tech.ikora.model.Argument;
import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class WaitUntilPageContains extends LibraryKeyword {
    public WaitUntilPageContains(){
        this.type = Type.SYNCHRONISATION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Argument.Type[] getArgumentTypes() {
        return new Argument.Type[]{
                Argument.Type.LOCATOR,
                Argument.Type.STRING,
                Argument.Type.STRING
        };
    }
}
