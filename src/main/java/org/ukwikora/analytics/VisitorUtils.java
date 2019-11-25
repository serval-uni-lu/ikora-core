package org.ukwikora.analytics;
import org.ukwikora.model.*;


public class VisitorUtils {
    public static void traverseDependencies(StatementVisitor visitor, Node node, VisitorMemory memory){
        for(Node dependency: node.getDependencies()){
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
        assignment.getKeywordCall().ifPresent(call ->
                call.getKeyword().ifPresent(keyword ->
                        keyword.accept(visitor, memory.getUpdated(assignment))
                )
        );
    }

    public static void traverseKeywordCall(StatementVisitor visitor, KeywordCall call, VisitorMemory memory){
        call.getKeyword().ifPresent(keyword ->{
            if(call.hasKeywordParameters()){
                for(Step step: call.getKeywordParameter()){
                    if(step == null){
                        continue;
                    }

                    step.accept(visitor, memory.getUpdated(step));
                }
            }

            keyword.accept(visitor, memory.getUpdated(keyword));
        });
    }
}
