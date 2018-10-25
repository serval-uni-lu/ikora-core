package org.ukwikora.model;

import java.util.Collections;
import java.util.List;

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

    public Step getSetup(){
        return setup;
    }

    public Step getTeadDown(){
        return tearDown;
    }

    @Override
    public List<TestCase> getTestCases(){
        return Collections.singletonList(this);
    }
}
