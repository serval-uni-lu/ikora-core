package lu.uni.serval.utils;

import lu.uni.serval.analytics.Action;

import java.util.ArrayList;
import java.util.List;

public class DifferentiableString implements Differentiable {
    private String text;

    public DifferentiableString(String text){
        this.text = text;
    }

    @Override
    public double distance(Differentiable other) {
        if(!(other instanceof DifferentiableString)){
            return 1;
        }

        DifferentiableString otherDifferentiableString = (DifferentiableString)other;

        return LevenshteinDistance.stringIndex(this.text, otherDifferentiableString.text);
    }

    @Override
    public List<Action> differences(Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(other instanceof DifferentiableString && ((DifferentiableString)other).text.equals(this.text)){
            return actions;
        }

        actions.add(Action.changeName(this, other));

        return actions;
    }
}
