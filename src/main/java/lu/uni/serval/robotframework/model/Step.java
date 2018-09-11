package lu.uni.serval.robotframework.model;

import java.util.*;


public abstract class Step implements Keyword {
    private Argument name;
    private Keyword parent;
    private String file;

    public Step() {

    }

    public abstract int getSize();
    public abstract void getSequences(List<List<Keyword>> sequences);

    public void setName(String name) {
        this.name = new Argument(name);
    }

    public void setParent(KeywordDefinition parent) {
        this.parent = parent;
    }

    public Argument getName() {
        return this.name;
    }

    public Keyword getParent() {
        return parent;
    }

    public abstract List<Argument> getParameters();

    @Override
    public Set<Keyword> getDependencies() {
        Set<Keyword> dependencies = new HashSet<>();
        dependencies.add(this.parent);

        return dependencies;
    }

    @Override
    public void addDependency(Keyword keyword) {
        this.parent = keyword;
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
    public void setFile(String file){
        this.file = file;
    }

    @Override
    public String getFile() {
        return this.file;
    }

    @Override
    public boolean matches(String name){
        return getName().matches(name);
    }

    @Override
    public Argument.Type[] getArgumentTypes() {
        return new Argument.Type[0];
    }

    @Override
    public int getMaxArgument(){
        return 0;
    }

    @Override
    public int[] getKeywordsLaunchedPosition() {
        return new int[0];
    }

    @Override
    public boolean isAction(){
        return false;
    }

    @Override
    public boolean isControlFlow(){
        return false;
    }

    @Override
    public boolean isSynchronisation(){
        return false;
    }

    @Override
    public boolean isCall(){
        return false;
    }
}
