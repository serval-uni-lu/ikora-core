package lu.uni.serval.robotframework.execution;

import lu.uni.serval.robotframework.report.Report;
import lu.uni.serval.robotframework.report.Result;
import lu.uni.serval.utils.KeywordData;
import lu.uni.serval.utils.TreeNode;
import lu.uni.serval.utils.exception.InvalidNumberArgumentException;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Runner {
    private Dispatcher dispatcher;

    public Runner() {
        this.dispatcher = new Dispatcher();
    }

    public Result execute(String keyword, List<String> arguments) {
        ImmutablePair<Object, Method> method = this.dispatcher.callMethod(keyword);
        Result result = null;

        if(method == null){
            System.err.println("Method '" + keyword + "' not found");
        }
        else  {
            try {
                int expect = method.getRight().getParameterTypes().length;
                int actual = arguments.size();

                if(expect > actual){
                    throw new InvalidNumberArgumentException(expect, actual);
                }
                else if(expect < actual) {
                    arguments = arguments.subList(0, expect);
                }

                result = (Result)method.getRight().invoke(method.getLeft(), arguments.toArray());
            }
            catch (IllegalAccessException e){
                result = new Result(Result.Type.Aborted);
                result.setError(Result.Error.IllegalAccess);
                result.setMessage(e.getMessage());
            }
            catch (InvocationTargetException e) {
                result = new Result(Result.Type.Aborted);
                result.setError(Result.Error.InvocationTarget);
                try {
                    throw e.getTargetException();
                } catch (Throwable throwable) {
                    System.out.println(throwable.getMessage());
                }
            }
            catch (InvalidNumberArgumentException e) {
                result = new Result(Result.Type.Aborted);
                result.setWrongNumberArugmentError(e.expected, e.actual);
            }
        }

        return result;
    }

    public Report executeKeyword(TreeNode<KeywordData> root) {
        Report report = new Report();

        for(TreeNode<KeywordData> leaf : root.getLeaves()) {
            Result result = this.execute(leaf.data.getCleanName(), leaf.data.getCleanArguments());
            report.addResult(result);
        }

        return report;
    }
}
