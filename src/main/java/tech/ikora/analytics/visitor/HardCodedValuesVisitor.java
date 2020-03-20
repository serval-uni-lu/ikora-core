package tech.ikora.analytics.visitor;

import tech.ikora.libraries.builtin.keywords.Sleep;
import tech.ikora.model.*;

import java.util.Optional;

public class HardCodedValuesVisitor extends DependencyVisitor {
    private int hardcodedValuesCounter = 0;

    public int getNumberHardcodedValues() {
        return hardcodedValuesCounter;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        for(Argument argument: call.getArgumentList()){
            final Optional<Node> definition = argument.getDefinition();

            if(definition.isPresent()){
                if(definition.get() instanceof Literal){
                    ++hardcodedValuesCounter;
                }
            }
        }

        VisitorUtils.traverseDependencies(this, call, memory);
    }
}
