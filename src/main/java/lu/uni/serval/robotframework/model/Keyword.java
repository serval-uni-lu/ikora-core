package lu.uni.serval.robotframework.model;

import lu.uni.serval.robotframework.runner.Runtime;

import java.util.Set;

public interface Keyword extends Element {
    Keyword getStep(int position);
    Argument getName();
    int getSize();

    void addDependency(Keyword keyword);

    void execute(Runtime runtime);

    Set<Keyword> getDependencies();

    Argument.Type[] getArgumentTypes();
    int getMaxArgument();
    int[] getKeywordsLaunchedPosition();

    boolean isAction();
    boolean isControlFlow();
    boolean isSynchronisation();
    boolean isCall();
}
