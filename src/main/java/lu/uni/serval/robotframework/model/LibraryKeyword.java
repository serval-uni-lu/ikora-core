package lu.uni.serval.robotframework.model;

import lu.uni.serval.analytics.Action;
import lu.uni.serval.utils.Differentiable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class LibraryKeyword implements Keyword {
    public enum Type{
        ControlFlow, Assertion, Action, Log, Error, Synchronisation, Get,  Unknown
    }

    private Set<Keyword> dependencies;
    private TestCaseFile file;
    protected Type type;

    public LibraryKeyword() {
        this.dependencies = new HashSet<>();
        this.type = Type.Unknown;
    }

    public Type getType(){
        return this.type;
    }

    @Override
    public Keyword getStep(int position) {
        return null;
    }

    @Override
    public int getSize(){
        return 1;
    }

    @Override
    public int getDepth(){
        return 0;
    }

    @Override
    public Set<Keyword> getDependencies() {
        return dependencies;
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
    public void addDependency(Keyword keyword) {
        this.dependencies.add(keyword);
    }

    @Override
    public Argument getName(){
        return new Argument(toKeyword(this.getClass()));
    }

    public static String toKeyword(Class<? extends LibraryKeyword> libraryClass) {
        String name = libraryClass.getSimpleName();
        return name.replaceAll("([A-Z])", " $1").trim().toLowerCase();
    }

    @Override
    public double distance(Differentiable other){
        return other.getClass() == this.getClass() ? 0 : 1;
    }

    @Override
    public List<Action> differences(Differentiable other){
        return null;
    }

    @Override
    public TestCaseFile getFile(){
        return this.file;
    }

    @Override
    public String getFileName(){
        return this.file.getName();
    }

    @Override
    public void setFile(TestCaseFile file){
        this.file = file;
    }

    @Override
    public boolean matches(String name) {
        return this.getName().matches(name);
    }

    @Override
    public Argument.Type[] getArgumentTypes() {
        return new Argument.Type[0];
    }

    @Override
    public int getMaxArgument(){
        Argument.Type[] types = getArgumentTypes();

        if(types.length == 0){
            return 0;
        }

        if(types[types.length - 1] == Argument.Type.Kwargs){
            return -1;
        }

        return types.length;
    }

    @Override
    public int[] getKeywordsLaunchedPosition() {
        return new int[0];
    }
}
