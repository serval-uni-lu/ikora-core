package lu.uni.serval.robotframework.execution;

import lu.uni.serval.robotframework.report.Report;
import lu.uni.serval.robotframework.report.Result;
import lu.uni.serval.utils.KeywordData;
import lu.uni.serval.utils.TreeNode;
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
                result = (Result)method.getRight().invoke(method.getLeft(), arguments);
            }
            catch (IllegalAccessException e){
                e.getStackTrace();
            }
            catch (InvocationTargetException e) {
                e.getStackTrace();
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
