package lu.uni.serval.ikora.core.runner.executors.libary;

import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.runner.executors.BaseExecutor;

public class LogExecutor extends BaseExecutor {
    public LogExecutor(Runtime runtime) {
        super(runtime);
    }

    public void execute() {
        runtime.setMessage("INFO", "test");
    }
}
