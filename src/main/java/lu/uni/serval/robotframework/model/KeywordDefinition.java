package lu.uni.serval.robotframework.model;

import lu.uni.serval.analytics.Action;
import lu.uni.serval.robotframework.runner.Runtime;
import lu.uni.serval.utils.Differentiable;
import lu.uni.serval.utils.LevenshteinDistance;


import javax.annotation.Nonnull;
import java.util.*;

public class KeywordDefinition implements Keyword, Iterable<Step> {
    private String file;
    private Argument name;
    private String documentation;
    private Set<String> tags;
    private List<Step> steps;
    private Set<Keyword> dependencies;

    KeywordDefinition(){
        steps = new ArrayList<>();
        dependencies = new HashSet<>();
        tags = new HashSet<>();
        documentation = "";
    }

    public void setFile(String file){
        this.file = file;
    }

    public void setName(String name){
        this.name = new Argument(name);
    }

    public void addStep(Step step){
        this.steps.add(step);
        step.setParent(this);
    }

    public void addTag(String tag){
        this.tags.add(tag);
    }

    public void addDependency(KeywordDefinition keyword) {
        this.dependencies.add(keyword);
    }

    public String getFile() {
        return file;
    }

    private List<Step> getSteps() {
        return steps;
    }

    public void setDocumentation(String documentation){
        this.documentation = documentation;
    }

    public Argument getName() {
        return name;
    }

    public String getDocumentation() {
        return documentation;
    }


    public Set<String> getTags() {
        return tags;
    }

    @Override
    public Keyword getStep(int position) {
        if(steps.size() <= position){
            return null;
        }

        return steps.get(position);
    }

    @Override
    public Set<Keyword> getDependencies() {
        return dependencies;
    }

    @Override
    public void addDependency(Keyword keyword) {
        this.dependencies.add(keyword);
    }


    public boolean matches(String name) {
        return this.name.matches(name);
    }

    @Nonnull
    public Iterator<Step> iterator() {
        return steps.iterator();
    }

    @Override
    public void execute(Runtime runtime) {

    }

    public int getSize() {
        int size = 1;

        for(Step step: steps){
            size += step.getSize();
        }

        return size;
    }

    public List<List<Keyword>> getSequences() {
        List<List<Keyword>> sequences = new ArrayList<>();
        sequences.add(new ArrayList<>());

        getSequences(sequences);

        return sequences;
    }

    void getSequences(List<List<Keyword>> sequences){
        for(Step step: steps){
            step.getSequences(sequences);
        }
    }

    @Override
    public double distance(Differentiable other) {
        return 0;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(!(other.getClass() == this.getClass())){
            return actions;
        }

        KeywordDefinition keyword = (KeywordDefinition)other;

        // check name change
        if(!this.getName().toString().equalsIgnoreCase(keyword.getName().toString())){
            actions.add(Action.changeName());
        }

        // check step changes
        List<Action> stepActions = computeStepsDifferences(keyword);
        actions.addAll(stepActions);

        return actions;
    }

    private List<Action> computeStepsDifferences(KeywordDefinition other) {
        List<Action> actions = new ArrayList<>();

        double[][] distances = LevenshteinDistance.distanceMatrix(this.getSteps(), other.getSteps());

        int xPosition = this.getSteps().size();
        int yPosition = other.getSteps().size();

        double value = distances[xPosition][yPosition];
        double initialValue = value;

        while(value != 0){
            double substitution = xPosition > 0 && yPosition > 0 ? distances[xPosition - 1][yPosition - 1] : initialValue;
            double addition = yPosition > 0 ? distances[xPosition][yPosition - 1] : initialValue;
            double subtraction = xPosition > 0 ? distances[xPosition - 1][yPosition] : initialValue;

            if(substitution < subtraction && substitution < addition){
                if(value > substitution){
                    Step thisStep = (Step)this.getStep(xPosition - 1);
                    Step otherStep = (Step)other.getStep(yPosition - 1);

                    actions.addAll(thisStep.differences(otherStep));
                }

                value = substitution;
                xPosition -= 1;
                yPosition -= 1;
            }
            else if (subtraction < addition){
                actions.add(Action.removeStep(this.getStep(xPosition - 1)));

                value = subtraction;
                xPosition -= 1;
            }
            else{
                actions.add(Action.insertStep(other.getStep(yPosition - 1)));

                value = addition;
                yPosition -= 1;
            }
        }

        return actions;
    }

    @Override
    public Argument.Type[] getArgumentTypes() {
        return new Argument.Type[0];
    }

    @Override
    public int getMaxArgument() {
        return 0;
    }

    @Override
    public int[] getKeywordsLaunchedPosition() {
        return new int[0];
    }

    @Override
    public boolean isAction() {
        return false;
    }

    @Override
    public boolean isControlFlow() {
        return false;
    }
}
