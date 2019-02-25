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

    public static void traverseAssignmentCall(StatementVisitor visitor, Assignment assignment, VisitorMemory memory){
        if(assignment.getExpression() != null && assignment.getExpression().getKeyword() != null){
            assignment.getExpression().getKeyword().accept(visitor, memory);
        }
    }

    public static void traverseKeywordCall(StatementVisitor visitor, KeywordCall call, VisitorMemory memory){
        if(call.getKeyword() == null){
            return;
        }

        if(call.hasKeywordParameters()){
            for(Step step: call.getKeywordParameter()){
                if(step == null){
                    continue;
                }

                step.accept(visitor, memory.getUpdated(step));
            }
        }

        call.getKeyword().accept(visitor, memory.getUpdated(call.getKeyword()));
    }
}
