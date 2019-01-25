package org.ukwikora.utils;

import org.ukwikora.model.*;

public class VisitorUtils {
    public static void traverseDependencies(StatementVisitor visitor, Statement statement){
        for(Statement dependency: statement.getDependencies()){
            if(visitor.isAcceptable(dependency)){
                dependency.accept(visitor);
            }
        }
    }

    public static void traverseSteps(StatementVisitor visitor, KeywordDefinition keyword){
        for(Step step: keyword.getSteps()){
            if(visitor.isAcceptable(step)){
                step.accept(visitor);
            }
        }
    }

    public static void traverseForLoopSteps(StatementVisitor visitor, ForLoop forLoop){
        for(Step step: forLoop.getSteps()){
            if(visitor.isAcceptable(step)){
                step.accept(visitor);
            }
        }
    }
}
