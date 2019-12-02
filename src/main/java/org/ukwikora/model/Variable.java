package org.ukwikora.model;

import org.ukwikora.builder.VariableParser;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Variable implements Node {
    public enum Type{
        SCALAR, LIST, DICTIONARY
    }

    private SourceFile file;
    private LineRange lineRange;
    private Assignment assignment;
    private Set<Node> dependencies;

    protected String name;
    protected Pattern pattern;

    protected Variable() {
        this.dependencies = new HashSet<>();
    }

    public Variable(String name) {
        this.assignment = null;
        this.dependencies = new HashSet<>();

        setName(name);
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    @Override
    public void setFile(@Nonnull SourceFile file) {
        this.file = file;
    }

    public abstract String getValueAsString();

    public boolean isAssignment(){
        return this.assignment != null;
    }

    @Override
    public SourceFile getFile() {
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
    public String getLibraryName(){
        if(this.file == null){
            return "";
        }

        return this.file.getLibraryName();
    }

    @Override
    public long getEpoch(){
        return this.file.getEpoch();
    }

    @Override
    public Value getNameAsValue() {
        return new Value(this.name);
    }

    @Override
    public String getName() {
        return this.name;
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
    public Set<Node> getDependencies(){
        return dependencies;
    }

    @Override
    public void addDependency(@Nonnull Node dependency){
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

    @Override
    public boolean matches(@Nonnull String name) {
        String generic = Value.getGenericVariableName(name);

        Matcher matcher = pattern.matcher(generic);
        return matcher.matches();
    }

    public static Variable create(Value value) throws Exception {
        Optional<Variable> variable = VariableParser.parse(value.toString());

        if(variable.isPresent()){
            value.setVariable(value.toString(), variable.get());
        }
        else{
            throw new Exception(String.format("Failed to create variable from value '%s'", value.getName()));
        }

        return variable.get();
    }

    protected abstract void setName(String name);
    public abstract void addElement(String element);
    public abstract List<Value> getValues();
}
