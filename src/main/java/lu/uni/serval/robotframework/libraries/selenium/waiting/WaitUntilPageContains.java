package lu.uni.serval.robotframework.libraries.selenium.waiting;

import lu.uni.serval.robotframework.model.Argument;
import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;

public class WaitUntilPageContains extends LibraryKeyword {
    public WaitUntilPageContains(){
        this.type = Type.Synchronisation;
    }

    @Override
    public void execute(Runtime runtime) {

    }

    @Override
    public Argument.Type[] getArgumentTypes() {
        return new Argument.Type[]{
                Argument.Type.Locator,
                Argument.Type.String,
                Argument.Type.String
        };
    }
}
