package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.KeywordDefinition;
import lu.uni.serval.utils.Differentiable;
import lu.uni.serval.utils.LevenshteinDistance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TimeLine implements Differentiable, Iterable<Difference> {
    private List<Difference> sequence;
    private List<Action> actions;
    private Differentiable last;
    private Differentiable lastValid;

    TimeLine() {
        sequence = new ArrayList<>();
        actions = new ArrayList<>();
        last = null;
        lastValid = null;
    }

    public boolean add(Difference difference){
        if(difference == null){
            return false;
        }

        if(this.sequence.size() > 0 && this.last != difference.getLeft()){
            return false;
        }

        this.sequence.add(difference);
        addActions(difference);

        this.last = difference.getRight();
        this.lastValid = difference.getValue();

        return true;
    }

    private void addActions(Difference difference){
        for(Action action: difference.getActions()){
            if(!isCreation(action) && !isDeletion(action)){
                this.actions.add(action);
            }
        }
    }

    @Override
    public Iterator<Difference> iterator() {
        return sequence.iterator();
    }

    public int size() {
        return sequence.size();
    }

    public Difference get(int index){
        return sequence.get(index);
    }

    public String getType() {
        if(lastValid == null){
            return "";
        }

        return lastValid.getClass().getSimpleName();
    }

    public String getName(){
        if(lastValid == null){
            return "";
        }

        return lastValid.getName();
    }

    public List<Action> getActions() {
        return this.actions;
    }

    public Differentiable getLastValid() {
        return lastValid;
    }

    public boolean isKeywordDefinition(){
        if(lastValid == null){
            return false;
        }

        return KeywordDefinition.class.isAssignableFrom(lastValid.getClass());
    }

    public boolean hasChanged(){
        return !this.actions.isEmpty();
    }

    private boolean isCreation(Action action){
        return action.getType() == Action.Type.ADD_USER_KEYWORD
                || action.getType() == Action.Type.ADD_TEST_CASE
                || action.getType() == Action.Type.ADD_VARIABLE;
    }

    private boolean isDeletion(Action action){
        return action.getType() == Action.Type.REMOVE_USER_KEYWORD
                || action.getType() == Action.Type.REMOVE_TEST_CASE
                || action.getType() == Action.Type.REMOVE_VARIABLE;
    }

    @Override
    public double distance(Differentiable other) {
        if(other.getClass() != this.getClass()){
            return 1.0;
        }

        if(!((TimeLine)other).getType().equals(this.getType())){
            return 1.0;
        }

        return LevenshteinDistance.index(actions, ((TimeLine)other).actions);
    }

    @Override
    public List<Action> differences(Differentiable other) {
        List<Action> invalid = new ArrayList<>();
        invalid.add(Action.invalid(this, other));

        if(other.getClass() != this.getClass()){
            return invalid;
        }

        if(((TimeLine)other).lastValid.getClass() != this.lastValid.getClass()){
            return invalid;
        }

        return LevenshteinDistance.getDifferences(this.actions, ((TimeLine)other).actions);
    }
}
