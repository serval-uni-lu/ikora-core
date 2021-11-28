package lu.uni.serval.ikora.core.analytics.visitor;

import lu.uni.serval.ikora.core.model.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ValueVisitor extends EmptyVisitor {
    private final Map<SourceNode, Set<Node>> nodeToDefinitionMap = new HashMap<>();

    private Argument previousArgument = null;

    public Map<SourceNode, Set<Node>> getNodeToDefinitionMap() {
        return nodeToDefinitionMap;
    }

    private void addToMap(SourceNode origin, Node definition){
        nodeToDefinitionMap.putIfAbsent(origin, new HashSet<>());
        nodeToDefinitionMap.get(origin).add(definition);
    }

    @Override
    public void visit(Assignment assignment, VisitorMemory memory) {
        if(memory.isAcceptable(assignment)){
            memory.getUpdated(assignment);
            addToMap(assignment, assignment.getKeywordCall().orElse(null));
        }
    }

    @Override
    public void visit(ScalarVariable scalar, VisitorMemory memory) {
        traverse(scalar, memory);
    }

    @Override
    public void visit(DictionaryVariable dictionary, VisitorMemory memory) {
        traverse(dictionary, memory);
    }

    @Override
    public void visit(ListVariable list, VisitorMemory memory) {
        traverse(list, memory);
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory){
        final int position = previousArgument.getPosition();
        previousArgument = null;

        if(position <= call.getArgumentList().size()){
            final Argument argument = call.getArgumentList().get(position);
            addToMap(previousArgument, argument);
            argument.accept(this, memory);
        }
    }

    @Override
    public void visit(Argument argument, VisitorMemory memory) {
        previousArgument = argument;
        traverse(argument, memory);
    }

    @Override
    public void visit(VariableAssignment assignment, VisitorMemory memory) {
        traverse(assignment, memory);
    }

    private void traverse(Node node, VisitorMemory memory){
        memory = memory.getUpdated(node);

        if(Dependable.class.isAssignableFrom(node.getClass())){
            for(Node dependency: ((Dependable)node).getDependencies()){
                if(memory.isAcceptable(dependency)){
                    dependency.accept(this, memory.getUpdated(dependency));
                }
            }
        }
        else if(SourceNode.class.isAssignableFrom(node.getClass())){
            final SourceNode parent =  ((SourceNode)node).getAstParent();
            if(memory.isAcceptable(parent)){
                parent.accept(this, memory.getUpdated(parent));
            }
        }
    }
}
