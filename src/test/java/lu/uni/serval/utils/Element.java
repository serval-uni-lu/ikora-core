package lu.uni.serval.utils;

import lu.uni.serval.analytics.Action;

import java.util.ArrayList;
import java.util.List;

public class Element implements Differentiable {
    private String element;

    Element(String string){
        element = string;
    }

    @Override
    public double distance(Differentiable other) {
        if(!(other instanceof Element)){
            return 1;
        }

        Element otherElement = (Element)other;

        return LevenshteinDistance.stringIndex(this.element, otherElement.element);
    }

    @Override
    public List<Action> differences(Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(other instanceof Element && ((Element)other).element.equals(this.element)){
            return actions;
        }

        actions.add(Action.changeName());

        return actions;
    }
}
