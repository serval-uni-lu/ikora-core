package tech.ikora.model;

import tech.ikora.analytics.Edit;
import tech.ikora.utils.LevenshteinDistance;

import java.util.ArrayList;
import java.util.List;

public class Sequence implements Differentiable {
    private List<Step> steps;

    public Sequence(){
        this.steps = new ArrayList<>();
    }

    public void addStep(Step step){
        steps.add(step);
    }

    public int size(){
        return steps.size();
    }

    @Override
    public double distance(Differentiable other) {
        List<Edit> edits = differences(other);

        for(Edit edit : edits){
            if(edit.getType() == Edit.Type.INVALID){
                return 1.0;
            }
        }

        return (double) edits.size() / this.size();
    }

    @Override
    public List<Edit> differences(Differentiable other) {
        List<Edit> edits = new ArrayList<>();

        if(other == this){
            return edits;
        }

        if(!(other instanceof Sequence)){
            edits.add(Edit.invalid(this, other));
            return edits;
        }

        Sequence sequence = (Sequence)other;

        edits.addAll(LevenshteinDistance.getDifferences(this.steps, sequence.steps));

        return edits;
    }
}
