package lu.uni.serval.robotframework.execution;

import lu.uni.serval.robotframework.report.Report;
import lu.uni.serval.robotframework.report.Result;
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

    public Result execute(String keyword, List<String> arguments) {
        Result result;

        try {
            result = dispatcher.call(keyword, arguments);
            if(result.isExecution()) {
                dispatcher.checkRequests();
            }
        }
        catch (IllegalAccessException e){
            result = new Result(Result.Type.Aborted);
            result.setError(Result.Error.IllegalAccess);
            result.setMessage(e.getMessage());
        }
        catch (InvocationTargetException e) {
            result = new Result(Result.Type.Aborted);
            result.setError(Result.Error.InvocationTarget);
            result.setMessage("Invocation Target Error");

            try {
                throw e.getTargetException();
            } catch (Throwable throwable) {
                result.setMessage(throwable.getMessage());
            }
        }
        catch (InvalidNumberArgumentException e) {
            result = new Result(Result.Type.Aborted);
            result.setWrongNumberArugmentError(e.expected, e.received);
        }
        catch (Exception e) {
            result = new Result(Result.Type.Aborted);
            result.setMessage("Unknown exception occured");
        }

        return result;
    }

    public Report executeKeyword(TreeNode root) {
        Report report = new Report();

        try{
            for(TreeNode leaf : root.getLeaves()) {
                if(!(leaf.data instanceof KeywordData)){
                    throw new WrongClassException(KeywordData.class, leaf.data.getClass());
                }

                KeywordData keyword = (KeywordData)leaf.data;

                Result result = this.execute(keyword.getCleanName(), keyword.getCleanArguments());
                report.addResult(result);

                if(result.isAborted()) {
                    break;
                }
            }
        }
        catch (WrongClassException e){
            Result result = new Result(Result.Type.Aborted);
            result.setError(Result.Error.Implementation);
            result.setMessage(e.getMessage());
        }

        return report;
    }
}
