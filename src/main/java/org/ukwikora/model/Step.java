package org.ukwikora.model;

import org.apache.commons.io.FilenameUtils;

import javax.annotation.Nonnull;
import java.util.*;


public abstract class Step implements Keyword {
    private Value name;
    private Statement parent;
    private TestCaseFile file;
    private LineRange lineRange;

    public abstract void getSequences(List<Sequence> sequences);

    public void setName(@Nonnull String name) {
        this.name = new Value(name);
    }

    public Value getNameAsValue() {
        return this.name;
    }

    public String getName() {
        return this.name.toString();
    }

    public Statement getParent() {
        return parent;
    }

    public abstract List<Value> getParameters();

    public abstract Optional<Value> getParameter(int position, boolean resolved);

    public abstract boolean hasParameters();

    @Override
    public Set<Statement> getDependencies() {
        Set<Statement> dependencies = new HashSet<>();
        dependencies.add(this.parent);

        return dependencies;
    }

    @Override
    public void addDependency(@Nonnull Statement dependency) {
        if(dependency instanceof Keyword){
            this.parent = dependency;
        }
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Step)) {
            return false;
        }

        Step step = (Step)other;

        return name.equals(step.name);
    }

    @Override
    public void setFile(@Nonnull TestCaseFile file){
        this.file = file;
    }

    @Override
    public TestCaseFile getFile() {
        return this.file;
    }

    @Override
    public String getFileName(){
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
    public long getEpoch() {
        return this.file.getEpoch();
    }

    @Override
    public boolean matches(@Nonnull String name){
        return getName().matches(name);
    }

    @Override
    public Value.Type[] getArgumentTypes() {
        return new Value.Type[0];
    }

    @Override
    public int getMaxArgument(){
        return 0;
    }

    @Override
    public Type getType(){
        return Type.Unknown;
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
    public int getLoc(){
        return file.getLoc(lineRange);
    }

    public abstract boolean hasKeywordParameters();

    public abstract List<KeywordCall> getKeywordParameter();
}
