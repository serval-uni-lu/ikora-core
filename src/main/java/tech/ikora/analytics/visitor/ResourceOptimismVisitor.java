package tech.ikora.analytics.visitor;

import tech.ikora.libraries.builtin.keywords.Sleep;
import tech.ikora.model.Keyword;
import tech.ikora.model.KeywordCall;

import java.util.Optional;

public class ResourceOptimismVisitor extends DependencyVisitor {
    private int sleepCounter = 0;

    public int getNumberSleepCalls() {
        return sleepCounter;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        final Optional<Keyword> keyword = call.getKeyword();

        if(keyword.isPresent()){
            if(keyword.get() instanceof Sleep){
                ++sleepCounter;
            }
        }

        VisitorUtils.traverseDependencies(this, call, memory);
    }
}
