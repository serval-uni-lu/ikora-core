package lu.uni.serval.robotframework.execution;

import lu.uni.serval.robotframework.report.Report;
import lu.uni.serval.robotframework.report.Result;
import lu.uni.serval.utils.KeywordData;
import lu.uni.serval.utils.TreeNode;
import lu.uni.serval.utils.exception.InvalidNumberArgumentException;

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
        }
        catch (IllegalAccessException e){
            result = new Result(Result.Type.Aborted);
            result.setError(Result.Error.IllegalAccess);
            result.setMessage(e.getMessage());
        }
        catch (InvocationTargetException e) {
            result = new Result(Result.Type.Aborted);
            result.setError(Result.Error.InvocationTarget);
            result.setMessage("Unhandled Error");
        }
        catch (InvalidNumberArgumentException e) {
            result = new Result(Result.Type.Aborted);
            result.setWrongNumberArugmentError(e.expected, e.actual);
        }

        return result;
    }

    public Report executeKeyword(TreeNode<KeywordData> root) {
        Report report = new Report();

        for(TreeNode<KeywordData> leaf : root.getLeaves()) {
            Result result = this.execute(leaf.data.getCleanName(), leaf.data.getCleanArguments());
            report.addResult(result);

            if(result.isAborted()) {
                break;
            }
        }

        return report;
    }
}
