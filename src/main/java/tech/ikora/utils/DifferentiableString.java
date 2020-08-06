package tech.ikora.utils;

import tech.ikora.analytics.Edit;
import tech.ikora.model.Differentiable;

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
    public List<Edit> differences(Differentiable other) {
        List<Edit> edits = new ArrayList<>();

        if(other instanceof DifferentiableString && ((DifferentiableString)other).text.equals(this.text)){
            return edits;
        }

        edits.add(Edit.changeName(this, other));

        return edits;
    }
}