package lu.uni.serval.robotframework.libraries.selenium.element;

import lu.uni.serval.robotframework.model.Argument;
import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;

public class ClickLink extends LibraryKeyword {
    public ClickLink(){
        this.type = Type.Action;
    }

    @Override
    public void execute(Runtime runtime) {

    }

    @Override
    public Argument.Type[] getArgumentTypes() {
        return new Argument.Type[]{
                Argument.Type.Locator
        };
    }
}
