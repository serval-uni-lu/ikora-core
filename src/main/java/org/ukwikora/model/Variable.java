package org.ukwikora.model;

import javax.annotation.Nonnull;
import java.util.*;

public abstract class Variable implements Statement {
    private TestCaseFile file;
    private String name;
    private LineRange lineRange;
    private Assignment assignment;
    private Set<Statement> dependencies;

    public Variable() {
        this.assignment = null;
        this.dependencies = new HashSet<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    @Override
    public void setFile(@Nonnull TestCaseFile file) {
        this.file = file;
    }

    public abstract String getValueAsString();

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
    public boolean matches(@Nonnull String name) {
        return this.name.equalsIgnoreCase(name);
    }

    @Override
    public void setLineRange(@Nonnull LineRange lineRange){
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

    @Override
    public Set<Statement> getDependencies(){
        return dependencies;
    }

    @Override
    public void addDependency(@Nonnull Statement dependency){
        dependencies.add(dependency);
    }

    public Optional<List<Value>> getResolvedValues() {
        List<Value> values = new ArrayList<>();

        for(Value value: getValues()){
            Optional<List<Value>> resolvedValues = value.getResolvedValues();

            if(!resolvedValues.isPresent()){
                return Optional.empty();
            }

            values.addAll(resolvedValues.get());
        }

        return Optional.of(values);
    }

    public abstract void addElement(String element);
    public abstract List<Value> getValues();
}
