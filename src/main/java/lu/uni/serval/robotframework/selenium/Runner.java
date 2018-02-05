package lu.uni.serval.robotframework.selenium;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Runner {
    private Dispatcher dispatcher;

    public Runner() {
        this.dispatcher = new Dispatcher();
    }

    public void execute(String keyword, List<String> arguments) {
        ImmutablePair<Object, Method> method = this.dispatcher.callMethod(keyword);

        if(method == null){
            System.out.println("Method '" + keyword + "' not found");
        }

        try {
            method.getRight().invoke(method.getLeft(), arguments);
        }
        catch (IllegalAccessException e){
            e.getStackTrace();
        }
        catch (InvocationTargetException e) {
            e.getStackTrace();
        }
    }
}
