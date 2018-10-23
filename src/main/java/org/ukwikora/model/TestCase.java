package org.ukwikora.model;

public class TestCase extends KeywordDefinition {
    private Step setup;
    private Step tearDown;

    @Override
    public int getConnectivity(int distance){
        return 0;
    }

    public void setSetup(Step setup){
        this.setup = setup;
    }

    public void setTeadDown(Step tearDown){
        this.tearDown = tearDown;
    }

    Step getSetup(){
        return setup;
    }

    Step getTeadDown(){
        return tearDown;
    }
}
