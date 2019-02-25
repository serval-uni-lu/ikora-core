package org.ukwikora.analytics;
import org.ukwikora.model.*;


public class VisitorUtils {
    public static void traverseDependencies(StatementVisitor visitor, Statement statement, VisitorMemory memory){
        for(Statement dependency: statement.getDependencies()){
            if(memory.isAcceptable(dependency)){
                dependency.accept(visitor, memory.getUpdated(dependency));
            }
        }
    }

    public static void traverseSteps(StatementVisitor visitor, KeywordDefinition keyword, VisitorMemory memory){
        for(Step step: keyword.getSteps()){
            if(memory.isAcceptable(step)){
                step.accept(visitor, memory.getUpdated(step));
            }
        }
    }

    public static void traverseForLoopSteps(StatementVisitor visitor, ForLoop forLoop, VisitorMemory memory){
        for(Step step: forLoop.getSteps()){
            if(memory.isAcceptable(step)){
                step.accept(visitor, memory.getUpdated(step));
            }
        }
    }
}
