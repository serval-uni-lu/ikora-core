package tech.ikora.analytics.visitor;
import tech.ikora.model.*;

import java.util.Optional;


public class VisitorUtils {
    private VisitorUtils(){}

    public static void traverseDependencies(NodeVisitor visitor, Node node, VisitorMemory memory){
        memory = memory.getUpdated(node);

        if(Dependable.class.isAssignableFrom(node.getClass())){
            for(Node dependency: ((Dependable)node).getDependencies()){
                if(memory.isAcceptable(dependency)){
                    dependency.accept(visitor, memory.getUpdated(dependency));
                }
            }
        }
        else if(SourceNode.class.isAssignableFrom(node.getClass())){
            final SourceNode parent =  ((SourceNode)node).getAstParent();
            if(memory.isAcceptable(parent)){
                parent.accept(visitor, memory.getUpdated(parent));
            }
        }
    }

    public static void traverseSteps(NodeVisitor visitor, KeywordDefinition keyword, VisitorMemory memory){
       memory = memory.getUpdated(keyword);

       for(Step step: keyword.getSteps()){
            if(memory.isAcceptable(step)){
                step.accept(visitor, memory.getUpdated(step));
            }
        }
    }

    public static void traverseForLoopSteps(NodeVisitor visitor, ForLoop forLoop, VisitorMemory memory){
        memory = memory.getUpdated(forLoop);

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
        memory = memory.getUpdated(call);

        final Optional<Keyword> keyword = call.getKeyword();

        if(keyword.isPresent()){
            for(Argument argument: call.getArgumentList()){
                if(memory.isAcceptable(argument)){
                    argument.accept(visitor, memory.getUpdated(argument));
                }
            }

            if(memory.isAcceptable(keyword.get())){
                keyword.get().accept(visitor, memory.getUpdated(keyword.get()));
            }
        }
    }

    public static void traverseArgument(NodeVisitor visitor, Argument argument, VisitorMemory memory){
        if(memory.isAcceptable(argument.getDefinition())) {
            argument.getDefinition().accept(visitor, memory.getUpdated(argument));
        }
    }

    public static void traverseValues(TreeVisitor treeVisitor, VariableAssignment variableAssignment, VisitorMemory memory) {
        for(SourceNode value: variableAssignment.getValues()){
            if(memory.isAcceptable(value)) {
                value.accept(treeVisitor, memory);
            }
        }
    }
}
