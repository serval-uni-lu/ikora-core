package lu.uni.serval.robotframework.libraries.builtin;

import lu.uni.serval.robotframework.model.Argument;
import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;

public class RunKeywords extends LibraryKeyword {
    public RunKeywords(){
        this.type = Type.Call;
    }

    @Override
    public void execute(Runtime runtime) {

    }

    @Override
    public Argument.Type[] getArgumentTypes() {
        return new Argument.Type[]{
                Argument.Type.Keywords,
        };
    }

    @Override
    public int[] getKeywordsLaunchedPosition() {
        return new int[]{-1};
    }
}
