package org.ukwikora.model;

import org.ukwikora.analytics.Action;
import org.ukwikora.analytics.NodeVisitor;
import org.ukwikora.analytics.VisitorMemory;
import org.ukwikora.runner.Runtime;

import javax.annotation.Nonnull;
import java.util.*;

public abstract class LibraryKeyword implements Keyword {

    private Set<Node> dependencies;
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
    public Set<Node> getDependencies() {
        return dependencies;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public long getEpoch() {
        return file.getEpoch();
    }

    @Override
    public void addDependency(@Nonnull Node dependency) {
        this.dependencies.add(dependency);
    }

    @Override
    public boolean isDeadCode(){
        return false;
    }

    @Override
    public String getName(){
        return toKeyword(this.getClass());
    }

    @Override
    public Value getNameAsValue(){
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
    public String getLibraryName(){
        String[] packages = this.getClass().getCanonicalName().split("\\.");

        for(int i = 0; i < packages.length; ++i){
            if(packages[i].equalsIgnoreCase("libraries") && i < packages.length - 1){
                return packages[i + 1];
            }
        }

        return "";
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

    @Override
    public void execute(Runtime runtime) throws Exception{
        runtime.enterKeyword(this);

        run(runtime);

        runtime.exitKeyword(this);
    }

    protected abstract void run(Runtime runtime);

    @Override
    public String getDocumentation(){
        return "";
    }

    @Override
    public List<Value> getReturnValues(){
        return Collections.emptyList();
    }
}
