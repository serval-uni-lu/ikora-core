package org.ukwikora.model;

import org.ukwikora.analytics.NodeVisitor;
import org.ukwikora.analytics.VisitorMemory;

import java.util.Collections;
import java.util.List;

public class TestCase extends KeywordDefinition {
    private KeywordCall setup;
    private KeywordCall tearDown;

    public void setSetup(KeywordCall setup){
        this.setup = setup;
    }

    public void setSetup(Step step){
        setSetup(toCall(step));
    }

    public void setTearDown(KeywordCall tearDown){
        this.tearDown = tearDown;
    }

    public void setTearDown(Step tearDown){
        setTearDown(toCall(tearDown));
    }

    public KeywordCall getSetup(){
        return setup;
    }

    public KeywordCall getTearDown(){
        return tearDown;
    }

    @Override
    public boolean isDeadCode(){
        return false;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public List<Value> getReturnValues() {
        return Collections.emptyList();
    }
}
