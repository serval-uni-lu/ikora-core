package lu.uni.serval.robotframework.model;

import lu.uni.serval.analytics.Action;
import lu.uni.serval.utils.Differentiable;
import lu.uni.serval.utils.LevenshteinDistance;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Variable implements Element {
    private TestCaseFile file;
    private String name;
    private List<Value> definition;
    private LineRange lineRange;
    private Assignment assignment;

    public Variable() {
        this.definition = new ArrayList<>();
        this.assignment = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addValueElement(String element) {
        this.definition.add(new Value(element));
    }

    public void setAssignment(Assignment assignment) {
        if(!this.definition.isEmpty()){
            throw new InvalidParameterException("Assignment return cannot already be pointing to values");
        }

        this.assignment = assignment;
    }

    @Override
    public void setFile(TestCaseFile file) {
        this.file = file;
    }

    public boolean isAssignment(){
        return this.assignment != null;
    }

    @Override
    public TestCaseFile getFile() {
        return this.file;
    }

    @Override
    public String getFileName() {
        if(this.file == null){
            return "";
        }

        return this.file.getName();
    }

    @Override
    public long getEpoch(){
        return this.file.getEpoch();
    }

    @Override
    public Value getNameAsArgument() {
        return new Value(this.name);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean matches(String name) {
        return this.name.equalsIgnoreCase(name);
    }

    public List<Value> getValue() {
        return definition;
    }

    public String getValueAsString(){
        StringBuilder builder = new StringBuilder();

        for(Iterator<Value> i = definition.iterator(); i.hasNext();) {
            builder.append(i.next().toString());

            if(i.hasNext()){
                builder.append("\t");
            }
        }

        return builder.toString();
    }

    @Override
    public double distance(Differentiable other) {
        if(!(other instanceof Variable)){
            return 1;
        }

        Variable variable = (Variable)other;
        double value = name.equals(variable.name) ? 0 : 0.5;
        return value + (LevenshteinDistance.index(definition, variable.definition) / 2.0);
    }

    @Override
    public List<Action> differences(Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(!(other instanceof Variable)){
            return actions;
        }

        Variable variable = (Variable)other;

        if(LevenshteinDistance.index(definition, variable.definition) > 0){
            actions.add(Action.changeVariableDefinition(this, other));
        }

        return actions;
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
        return file.getLoc(lineRange);
    }
}
