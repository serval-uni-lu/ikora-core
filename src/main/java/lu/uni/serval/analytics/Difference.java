package lu.uni.serval.analytics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lu.uni.serval.robotframework.model.*;
import lu.uni.serval.utils.Differentiable;
import lu.uni.serval.utils.LevenshteinDistance;

import java.util.*;

@JsonSerialize(using = DifferenceSerializer.class)
public class Difference implements Differentiable {
    enum Clone{
        TypeI, TypeII, None
    }

    private Differentiable left;
    private Differentiable right;
    private List<Action> actions;

    static private Set<Action.Type> ignoreForTypeI;
    static private Set<Action.Type> ignoreForTypeII;

    static {
        Set<Action.Type> typeI = new HashSet<>(4);
        typeI.add(Action.Type.CHANGE_NAME);
        typeI.add(Action.Type.CHANGE_DOCUMENTATION);
        typeI.add(Action.Type.REMOVE_DOCUMENTATION);
        typeI.add(Action.Type.ADD_DOCUMENTATION);
        ignoreForTypeI = Collections.unmodifiableSet(typeI);

        Set<Action.Type> typeII = new HashSet<>(6);
        typeII.add(Action.Type.CHANGE_NAME);
        typeII.add(Action.Type.CHANGE_DOCUMENTATION);
        typeII.add(Action.Type.REMOVE_DOCUMENTATION);
        typeII.add(Action.Type.ADD_DOCUMENTATION);
        typeII.add(Action.Type.CHANGE_STEP_ARGUMENTS);
        typeII.add(Action.Type.CHANGE_STEP_RETURN_VALUES);
        ignoreForTypeII = Collections.unmodifiableSet(typeII);
    }

    private Difference(Differentiable left, Differentiable right){
        this.left = left;
        this.right = right;

        this.actions = new ArrayList<>();
    }

    public boolean isEmpty(){
        return actions.isEmpty();
    }

    public boolean isEmpty(Set<Action.Type> ignore){
        if(actions.isEmpty()){
            return true;
        }

        if(ignore.size() == 0){
            return isEmpty();
        }

        for(Action action: actions){
            if (!ignore.contains(action.getType())){
                return false;
            }
        }

        return true;
    }

    public boolean isCloneTypeI(){
        return isEmpty(ignoreForTypeI);
    }

    public boolean isCloneTypeII(){
        return isEmpty(ignoreForTypeII);
    }

    public Differentiable getLeft(){
        return left;
    }

    public Differentiable getRight(){
        return right;
    }

    public Differentiable getValue(){
        if(left != null){
            return left;
        }

        return right;
    }

    public List<Action> getActions() {
        return actions;
    }

    public static Difference of(Differentiable before, Differentiable after) {
        Difference difference = new Difference(before, after);

        if(before == after){
            return difference;
        }

        if(before == null){
            difference.actions.add(Action.addElement(after.getClass(), after));
            return difference;
        }

        if(after == null){
            difference.actions.add(Action.removeElement(before.getClass(), before));
            return difference;
        }

        difference.actions.addAll(before.differences(after));

        return difference;
    }

    @Override
    public boolean equals(Object other) {
        if(this == other){
            return true;
        }

        if(other == null){
            return false;
        }

        if(this.getClass() != other.getClass()){
            return false;
        }

        Difference difference = (Difference)other;

        return this.left == difference.left
                && this.right == difference.right;
    }

    @Override
    public int hashCode(){
        int hash = 7;
        hash = getNodeHash(hash, left);
        hash = getNodeHash(hash, right);

        return hash;
    }

    private int getNodeHash(int hash, Differentiable element){
        if(element == null){
            hash = 31 * hash;
        }
        else if(element instanceof KeywordDefinition){
            KeywordDefinition keyword = (KeywordDefinition)element;

            hash = 31 * hash + (keyword.getFile() == null ? 0 : keyword.getFile().hashCode());
            hash = 31 * hash + (keyword.getName() == null ? 0 : keyword.getName().hashCode());
        }
        else{
            hash = element.hashCode();
        }

        return hash;
    }

    @Override
    public double distance(Differentiable other) {
        if(other.getClass() != this.getClass()){
            return 1.0;
        }

        if(((Difference)other).getValue().getClass() != this.getValue().getClass()){
            return 1.0;
        }

        return LevenshteinDistance.index(this.actions, ((Difference)other).actions);
    }

    @Override
    public List<Action> differences(Differentiable other) {
        return null;
    }

    @Override
    public String getName() {
        return String.valueOf(this.hashCode());
    }

    public Class<?> getType() {
        return this.getValue().getClass();
    }
}
