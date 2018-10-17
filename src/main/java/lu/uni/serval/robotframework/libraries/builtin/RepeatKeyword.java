package lu.uni.serval.robotframework.libraries.builtin;

import lu.uni.serval.robotframework.model.Argument;
import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;

public class RepeatKeyword extends LibraryKeyword {
    public RepeatKeyword(){
        this.type = Type.ControlFlow;
    }

    @Override
    public void execute(Runtime runtime) {

    }

    @Override
    public Argument.Type[] getArgumentTypes() {
        return new Argument.Type[]{
                Argument.Type.String,
                Argument.Type.Keyword,
                Argument.Type.Kwargs
        };
    }

    @Override
    public int[] getKeywordsLaunchedPosition() {
        return new int[]{0};
    }
}
