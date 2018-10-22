package lu.uni.serval.robotframework.model;

import java.util.*;


public abstract class Step implements Keyword {
    private Value name;
    private Keyword parent;
    private TestCaseFile file;
    private LineRange lineRange;

    public Step() {

    }

    public abstract int getSize();
    public abstract void getSequences(List<Sequence> sequences);

    public void setName(String name) {
        this.name = new Value(name);
    }

    public void setParent(Keyword parent) {
        this.parent = parent;
    }

    public Value getNameAsArgument() {
        return this.name;
    }

    public String getName() {
        return this.name.toString();
    }

    public Keyword getParent() {
        return parent;
    }

    public abstract List<Value> getParameters();

    @Override
    public Set<Keyword> getDependencies() {
        Set<Keyword> dependencies = new HashSet<>();
        dependencies.add(this.parent);

        return dependencies;
    }

    @Override
    public int getConnectivity(int distance){
        if(distance == 0){
            return 0;
        }

        int size = 0;

        for(Keyword keyword: this.parent.getDependencies()){
            size += keyword.getConnectivity(distance - 1) + 1;
        }

        return size;
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
    public void setFile(TestCaseFile file){
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
    public long getEpoch() {
        return this.file.getEpoch();
    }

    @Override
    public boolean matches(String name){
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
    public int[] getKeywordsLaunchedPosition() {
        return new int[0];
    }

    @Override
    public Type getType(){
        return Type.Unknown;
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
    public int getLoc(){
        return file.getLoc(lineRange);
    }
}
