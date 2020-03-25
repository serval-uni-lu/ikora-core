package tech.ikora.analytics.visitor;
import tech.ikora.model.*;


public class VisitorUtils {
    private VisitorUtils(){}

    public static void traverseDependencies(NodeVisitor visitor, Node node, VisitorMemory memory){
        for(Node dependency: node.getDependencies()){
            if(memory.isAcceptable(dependency)){
                dependency.accept(visitor, memory.getUpdated(dependency));
            }
        }
    }

    public static void traverseSteps(NodeVisitor visitor, KeywordDefinition keyword, VisitorMemory memory){
        for(Step step: keyword.getSteps()){
            if(memory.isAcceptable(step)){
                step.accept(visitor, memory.getUpdated(step));
            }
        }
    }

    public static void traverseForLoopSteps(NodeVisitor visitor, ForLoop forLoop, VisitorMemory memory){
        for(Step step: forLoop.getSteps()){
            if(memory.isAcceptable(step)){
                step.accept(visitor, memory.getUpdated(step));
            }
        }
    }

    public static void traverseAssignmentCall(NodeVisitor visitor, Assignment assignment, VisitorMemory memory){
        assignment.getKeywordCall().flatMap(KeywordCall::getKeyword).ifPresent(keyword ->
                keyword.accept(visitor, memory.getUpdated(assignment))
        );
    }

    public static void traverseKeywordCall(NodeVisitor visitor, KeywordCall call, VisitorMemory memory){
        call.getKeyword().ifPresent(keyword ->{
            for(Argument argument: call.getArgumentList()){
                argument.accept(visitor, memory.getUpdated(argument));
            }

            keyword.accept(visitor, memory.getUpdated(keyword));
        });
    }

    public static void traverseArgument(NodeVisitor visitor, Argument argument, VisitorMemory memory){
        argument.getDefinition().ifPresent(keyword ->
                keyword.accept(visitor, memory.getUpdated(argument))
        );
    }

    public static void traverseValues(TreeVisitor treeVisitor, VariableAssignment variableAssignment, VisitorMemory memory) {
        for(Node value: variableAssignment.getValues()){
            value.accept(treeVisitor, memory);
        }
    }
}
