package lu.uni.serval.robotframework.execution;

import lu.uni.serval.robotframework.execution.selenium.BrowserManagement;
import lu.uni.serval.robotframework.execution.selenium.Context;
import lu.uni.serval.robotframework.execution.selenium.FormElement;
import lu.uni.serval.robotframework.execution.selenium.Keyword;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.lang.reflect.Method;
import java.util.*;

public class Dispatcher {
    private Map<String, ImmutablePair<Object, Method>> operations;
    private Context context;

    public Dispatcher(){
        this.operations = new HashMap<String, ImmutablePair<Object, Method>>();
        this.context = new Context();

        subscribeKeywords(new BrowserManagement(this.context));
        subscribeKeywords(new FormElement(this.context));
    }

    private void subscribeKeywords(Object object) {
        final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(object.getClass().getDeclaredMethods()));

        for (final Method method : allMethods) {
            if (method.isAnnotationPresent(Keyword.class)) {
                operations.put(toKeyword(method.getName()), ImmutablePair.of(object, method));
            }
        }
    }

    private String toKeyword(String method) {
        return method.replaceAll("([A-Z])", " $1").trim().toLowerCase();
    }

    public ImmutablePair<Object, Method> callMethod(String keyword) {
        return operations.get(keyword.trim().toLowerCase());
    }
}
