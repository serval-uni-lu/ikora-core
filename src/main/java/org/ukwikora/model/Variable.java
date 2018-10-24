package org.ukwikora.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Variable implements Element {
    private TestCaseFile file;
    private String name;
    private LineRange lineRange;
    private Assignment assignment;

    public Variable() {
        this.assignment = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    @Override
    public void setFile(TestCaseFile file) {
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
    public boolean matches(String name) {
        return this.name.equalsIgnoreCase(name);
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
