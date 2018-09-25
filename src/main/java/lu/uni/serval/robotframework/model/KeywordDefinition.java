package lu.uni.serval.robotframework.model;

import lu.uni.serval.analytics.Action;
import lu.uni.serval.robotframework.runner.Runtime;
import lu.uni.serval.utils.Differentiable;
import lu.uni.serval.utils.LevenshteinDistance;


import javax.annotation.Nonnull;
import java.util.*;

public class KeywordDefinition implements Keyword, Iterable<Step> {
    private TestCaseFile file;
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

    @Override
    public void setFile(TestCaseFile file) {
        this.file = file;
    }

    @Override
    public TestCaseFile getFile() {
        return file;
    }

    @Override
    public String getFileName() {
        if(this.file == null){
            return "";
        }

        return this.file.getName();
    }

    @Override
    public long getEpoch() {
        return file.getEpoch();
    }

    public List<Step> getSteps() {
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
    public int getConnectivity(int distance){
        if(distance == 0){
            return 0;
        }

        int size = 0;

        for(Keyword keyword: dependencies){
            size += keyword.getConnectivity(distance - 1) + 1;
        }

        return size;
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

    @Override
    public int getDepth() {
        int depth = 0;

        for(Step step: steps){
            depth = Math.max(step.getDepth(), depth);
        }

        return depth + 1;
    }

    List<List<Keyword>> getSequences() {
        List<List<Keyword>> sequences = new ArrayList<>();
        sequences.add(new ArrayList<>());

        getSequences(sequences);

        return sequences;
    }

    public int getMaxSequenceSize(){
        int size = 0;

        for(List<Keyword> sequence: getSequences()){
            size = Math.max(sequence.size(), size);
        }

        return size;
    }

    public int getBranchIndex(){
        return (int)Math.round(Math.log(getSequences().size()) / Math.log(2));
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
        List<Action> stepActions = LevenshteinDistance.getDifferences(this.getSteps(), keyword.getSteps());
        actions.addAll(stepActions);

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
    public Type getType(){
        return Type.User;
    }
}
