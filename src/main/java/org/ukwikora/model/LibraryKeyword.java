package org.ukwikora.model;

import org.ukwikora.analytics.Action;

import javax.annotation.Nonnull;
import java.util.*;

public abstract class LibraryKeyword implements Keyword {

    private Set<Keyword> dependencies;
    private TestCaseFile file;
    private LineRange lineRange;

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
    public int getLevel(){
        return 0;
    }

    @Override
    public Set<Keyword> getDependencies() {
        return dependencies;
    }

    @Override
    public List<TestCase> getTestCases() {
        List<TestCase> testCases = new ArrayList<>();

        for(Keyword dependency: getDependencies()){
            if (dependency instanceof TestCase){
                testCases.add((TestCase) dependency);
            }
            else{
                testCases.addAll(dependency.getTestCases());
            }
        }

        return testCases;
    }

    @Override
    public List<String> getSuites() {
        List<String> suites = new ArrayList<>();

        for(TestCase testCase: getTestCases()){
            suites.add(testCase.getFile().getName());
        }

        return suites;
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
    public long getEpoch() {
        return file.getEpoch();
    }

    @Override
    public void addDependency(@Nonnull Keyword keyword) {
        this.dependencies.add(keyword);
    }

    @Override
    public String getName(){
        return toKeyword(this.getClass());
    }

    @Override
    public Value getNameAsArgument(){
        return new Value(toKeyword(this.getClass()));
    }

    public static String toKeyword(Class<? extends LibraryKeyword> libraryClass) {
        String name = libraryClass.getSimpleName();
        return name.replaceAll("([A-Z])", " $1").trim().toLowerCase();
    }

    @Override
    public double distance(@Nonnull Differentiable other){
        return other.getClass() == this.getClass() ? 0 : 1;
    }

    @Override
    public List<Action> differences(@Nonnull Differentiable other){
        if(other.getClass() == this.getClass()){
            return Collections.emptyList();
        }

        return Collections.singletonList(Action.invalid(this, other));
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
    public void setFile(@Nonnull TestCaseFile file){
        this.file = file;
    }

    @Override
    public boolean matches(@Nonnull String name) {
        return this.getName().matches(name);
    }

    @Override
    public Value.Type[] getArgumentTypes() {
        return new Value.Type[0];
    }

    @Override
    public int getMaxArgument(){
        Value.Type[] types = getArgumentTypes();

        if(types.length == 0){
            return 0;
        }

        if(types[types.length - 1] == Value.Type.Kwargs){
            return -1;
        }

        return types.length;
    }

    @Override
    public int[] getKeywordsLaunchedPosition() {
        return new int[0];
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
        return 1;
    }
}
