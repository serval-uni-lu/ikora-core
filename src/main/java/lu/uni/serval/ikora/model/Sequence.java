package lu.uni.serval.ikora.model;

import java.util.ArrayList;
import java.util.List;

public class Sequence {
    private final List<Step> steps;

    public Sequence(){
        this.steps = new ArrayList<>();
    }

    public void addStep(Step step){
        steps.add(step);
    }

    public int size(){
        return steps.size();
    }
}
