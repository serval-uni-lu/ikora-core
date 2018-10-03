package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.Element;
import lu.uni.serval.robotframework.model.KeywordDefinition;
import lu.uni.serval.utils.Differentiable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TimeLine implements Iterable<Difference> {
    private List<Difference> sequence;
    private Differentiable last;

    TimeLine() {
        sequence = new ArrayList<>();
        last = null;
    }

    public boolean add(Difference difference){
        if(difference == null){
            return false;
        }

        if(this.sequence.size() > 0 && this.last != difference.getLeft()){
            return false;
        }

        this.sequence.add(difference);
        this.last = difference.getRight();

        return true;
    }

    public Differentiable getLast(){
        return this.last;
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
        Differentiable differentiable = firstDifferentiable();

        if(differentiable == null){
            return "";
        }

        return differentiable.getClass().getSimpleName();
    }

    public String getName(){
        Differentiable differentiable = firstDifferentiable();

        if(differentiable == null){
            return "";
        }

        if(!Element.class.isAssignableFrom(differentiable.getClass())){
            return "";
        }

        return ((Element)differentiable).getName().toString();
    }

    public boolean isKeywordDefinition(){
        Differentiable differentiable = firstDifferentiable();

        if(differentiable == null){
            return false;
        }

        return KeywordDefinition.class.isAssignableFrom(differentiable.getClass());
    }

    private Differentiable firstDifferentiable(){
        if(size() == 0){
            return null;
        }

        Difference difference = sequence.get(0);

        return difference.getValue();
    }
}
