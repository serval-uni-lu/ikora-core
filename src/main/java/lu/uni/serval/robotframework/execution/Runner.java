package lu.uni.serval.robotframework.execution;

import lu.uni.serval.robotframework.report.ExecutionReport;
import lu.uni.serval.robotframework.report.ExecutionResult;
import lu.uni.serval.utils.KeywordData;
import lu.uni.serval.utils.tree.TreeNode;
import lu.uni.serval.utils.exception.InvalidNumberArgumentException;
import lu.uni.serval.utils.exception.WrongClassException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Runner {
    private Dispatcher dispatcher;

    public Runner() {
        this.dispatcher = new Dispatcher();
    }

    public ExecutionResult execute(String keyword, List<String> arguments) {
        ExecutionResult result;

        try {
            result = dispatcher.call(keyword, arguments);
            if(result.isExecution()) {
                dispatcher.checkRequests();
            }
        }
        catch (IllegalAccessException e){
            result = new ExecutionResult(ExecutionResult.Type.Aborted);
            result.setError(ExecutionResult.Error.IllegalAccess);
            result.setMessage(e.getMessage());
        }
        catch (InvocationTargetException e) {
            result = new ExecutionResult(ExecutionResult.Type.Aborted);
            result.setError(ExecutionResult.Error.InvocationTarget);
            result.setMessage("Invocation Target Error");

            try {
                throw e.getTargetException();
            } catch (Throwable throwable) {
                result.setMessage(throwable.getMessage());
            }
        }
        catch (InvalidNumberArgumentException e) {
            result = new ExecutionResult(ExecutionResult.Type.Aborted);
            result.setWrongNumberArugmentError(e.expected, e.received);
        }
        catch (Exception e) {
            result = new ExecutionResult(ExecutionResult.Type.Aborted);
            result.setMessage("Unknown exception occured");
        }

        return result;
    }

    public ExecutionReport executeKeyword(TreeNode root) {
        ExecutionReport report = new ExecutionReport();

        try{
            for(TreeNode leaf : root.getLeaves()) {
                if(!(leaf.data instanceof KeywordData)){
                    throw new WrongClassException(KeywordData.class, leaf.data.getClass());
                }

                KeywordData keyword = (KeywordData)leaf.data;

                ExecutionResult result = this.execute(keyword.getCleanName(), keyword.getCleanArguments());
                report.addResult(result);

                if(result.isAborted()) {
                    break;
                }
            }
        }
        catch (WrongClassException e){
            ExecutionResult result = new ExecutionResult(ExecutionResult.Type.Aborted);
            result.setError(ExecutionResult.Error.Implementation);
            result.setMessage(e.getMessage());
        }

        return report;
    }
}
