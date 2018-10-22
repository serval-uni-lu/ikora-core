package lu.uni.serval.robotframework.libraries.selenium.waiting;

import lu.uni.serval.robotframework.model.Value;
import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;

public class WaitUntilElementIsVisible extends LibraryKeyword {
    public WaitUntilElementIsVisible(){
        this.type = Type.Synchronisation;
    }

    @Override
    public void execute(Runtime runtime) {

    }

    @Override
    public Value.Type[] getArgumentTypes() {
        return new Value.Type[]{
                Value.Type.Locator,
                Value.Type.String,
                Value.Type.String
        };
    }
}
