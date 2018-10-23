package lu.uni.serval.robotframework.model;

import lu.uni.serval.robotframework.analytics.Action;
import lu.uni.serval.robotframework.analytics.ReportAnalyzer;
import lu.uni.serval.robotframework.analytics.StatusResults;
import lu.uni.serval.robotframework.runner.Runtime;
import lu.uni.serval.robotframework.utils.LevenshteinDistance;


import javax.annotation.Nonnull;
import java.util.*;

public class KeywordDefinition implements Keyword, Iterable<Step> {
    private TestCaseFile file;
    private Value name;
    private String documentation;
    private Set<String> tags;
    private List<Step> steps;
    private Set<Keyword> dependencies;
    private LineRange lineRange;

    KeywordDefinition(){
        steps = new ArrayList<>();
        dependencies = new HashSet<>();
        tags = new HashSet<>();
        documentation = "";
    }

    public void setName(String name){
        this.name = new Value(name);
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

        for(Step step: this.steps){
            step.setFile(this.file);
        }
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

    @Override
    public String getName() {
        return name.toString();
    }

    @Override
    public Value getNameAsArgument() {
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

    List<Sequence> getSequences() {
        List<Sequence> sequences = new ArrayList<>();
        sequences.add(new Sequence());

        getSequences(sequences);

        return sequences;
    }

    public int getMaxSequenceSize(){
        Sequence maxSequence = getMaxSequence();

        if(maxSequence == null){
            return 0;
        }

        return maxSequence.size();
    }

    public Sequence getMaxSequence() {
        Sequence maxSequence = null;

        for(Sequence sequence: getSequences()){
            if(maxSequence == null){
                maxSequence = sequence;
            }
            else if (maxSequence.size() < sequence.size()){
                maxSequence = sequence;
            }
        }

        return maxSequence;
    }

    public int getBranchIndex(){
        return (int)Math.round(Math.log(getSequences().size()) / Math.log(2));
    }

    void getSequences(List<Sequence> sequences){
        for(Step step: steps){
            step.getSequences(sequences);
        }
    }

    @Override
    public double distance(StatusResults.Differentiable other) {
        return (double)differences(other).size() / this.getLoc();
    }

    @Override
    public List<Action> differences(StatusResults.Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(other == this){
            return actions;
        }

        if(other.getClass() != this.getClass()){
            actions.add(Action.invalid(this, other));
            return actions;
        }

        KeywordDefinition keyword = (KeywordDefinition)other;

        // check name change
        if(!this.getName().toString().equalsIgnoreCase(keyword.getName().toString())){
            actions.add(Action.changeName(this, other));
        }

        // check documentation change
        Action documentationAction = differenceDocumentation(keyword);
        if(documentationAction != null){
            actions.add(documentationAction);
        }

        // check step changes
        List<Action> stepActions = LevenshteinDistance.getDifferences(this.getSteps(), keyword.getSteps());
        actions.addAll(stepActions);

        return actions;
    }

    private Action differenceDocumentation(KeywordDefinition keyword) {
        Action action = null;

        if(this.documentation.isEmpty() && !keyword.documentation.isEmpty()){
            action = Action.addDocumentation(this, keyword);
        }
        else if(!this.documentation.isEmpty() && keyword.documentation.isEmpty()){
            action = Action.removeDocumentation(this, keyword);
        }
        else if(!this.documentation.equals(keyword.documentation)){
            action = Action.changeDocumentation(this, keyword);
        }

        return action;
    }

    @Override
    public Value.Type[] getArgumentTypes() {
        return new Value.Type[0];
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

    @Override
    public void setLineRange(LineRange lineRange){
        this.lineRange = lineRange;
    }

    @Override
    public LineRange getLineRange(){
        return this.lineRange;
    }

    @Override
    public int getLoc() {
        return this.file.getLoc(this.lineRange);
    }
}
