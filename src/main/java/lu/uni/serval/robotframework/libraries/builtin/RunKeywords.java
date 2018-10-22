package lu.uni.serval.robotframework.libraries.builtin;

import lu.uni.serval.robotframework.model.Value;
import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;

public class RunKeywords extends LibraryKeyword {
    public RunKeywords(){
        this.type = Type.ControlFlow;
    }

    @Override
    public void execute(Runtime runtime) {

    }

    @Override
    public Value.Type[] getArgumentTypes() {
        return new Value.Type[]{
                Value.Type.Keywords,
        };
    }

    @Override
    public int[] getKeywordsLaunchedPosition() {
        return new int[]{-1};
    }
}
