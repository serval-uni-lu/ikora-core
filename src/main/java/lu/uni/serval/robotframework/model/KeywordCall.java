package lu.uni.serval.robotframework.model;

import java.util.List;

public interface KeywordCall {
    void setName(String name);
    void addArgument(String argument);

    Argument getName();
    List<Argument> getArguments();
}
