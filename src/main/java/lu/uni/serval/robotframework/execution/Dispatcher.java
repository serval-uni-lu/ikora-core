package lu.uni.serval.robotframework.execution;

import lu.uni.serval.robotframework.execution.selenium.BrowserManagement;
import lu.uni.serval.robotframework.execution.selenium.Context;
import lu.uni.serval.robotframework.execution.selenium.FormElement;
import lu.uni.serval.robotframework.execution.selenium.Keyword;
import lu.uni.serval.robotframework.report.ExecutionResult;
import lu.uni.serval.utils.EasyPair;
import lu.uni.serval.utils.exception.InvalidNumberArgumentException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Dispatcher {
    private Map<String, EasyPair<Object, Method>> operations;
    private Context context;

    public Dispatcher(){
        this.operations = new HashMap<>();
        this.context = new Context();

        subscribeKeywords(new BrowserManagement(this.context));
        subscribeKeywords(new FormElement(this.context));
    }

    private void subscribeKeywords(Object object) {
        final List<Method> allMethods = new ArrayList<>(Arrays.asList(object.getClass().getDeclaredMethods()));

        for (final Method method : allMethods) {
            if (method.isAnnotationPresent(Keyword.class)) {
                operations.put(toKeyword(method.getName()), EasyPair.of(object, method));
            }
        }
    }

    private String toKeyword(String method) {
        return method.replaceAll("([A-Z])", " $1").trim().toLowerCase();
    }

    public ExecutionResult call(String keyword, List<String> arguments)
            throws InvocationTargetException, IllegalAccessException, InvalidNumberArgumentException {

        EasyPair<Object, Method> method = operations.get(keyword.trim().toLowerCase());

        int expect = method.getRight().getParameterTypes().length;
        int actual = arguments.size();

        if(expect > actual){
            throw new InvalidNumberArgumentException(expect, actual);
        }
        else if(expect < actual) {
            arguments = arguments.subList(0, expect);
        }

        return (ExecutionResult)method.getRight().invoke(method.getLeft(), arguments.toArray());
    }

    public void checkRequests() {
        this.context.checkRequests();
    }
}
