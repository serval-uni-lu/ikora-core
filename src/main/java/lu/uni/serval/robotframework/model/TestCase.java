package lu.uni.serval.robotframework.model;

public class TestCase extends UserKeyword {
    private Step setup;
    private Step teadDown;

    public void setSetup(Step setup){
        this.setup = setup;
    }

    public void setTeadDown(Step tearDown){
        this.teadDown = tearDown;
    }

    Step getSetup(){
        return setup;
    }

    Step getTeadDown(){
        return teadDown;
    }
}
