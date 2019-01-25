package org.ukwikora.model;

public class TestCase extends KeywordDefinition {
    private Step setup;
    private Step tearDown;

    public void setSetup(Step setup){
        this.setup = setup;
    }

    public void setTeadDown(Step tearDown){
        this.tearDown = tearDown;
    }

    public Step getSetup(){
        return setup;
    }

    public Step getTeadDown(){
        return tearDown;
    }

    @Override
    public void accept(StatementVisitor visitor){
        visitor.visit(this);
    }
}
