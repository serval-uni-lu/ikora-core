package lu.uni.serval.robotframework.model;

import lu.uni.serval.analytics.Action;
import lu.uni.serval.utils.Differentiable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class LibraryKeyword implements Keyword {
    private Set<Keyword> dependencies;
    private String file;

    public LibraryKeyword() {
        this.dependencies = new HashSet<>();
    }

    @Override
    public Keyword getStep(int position) {
        return null;
    }

    @Override
    public Set<Keyword> getDependencies() {
        return dependencies;
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
    public int getSize() {
        return 1;
    }

    @Override
    public List<Keyword> getSequence() {
        List<Keyword> sequence = new ArrayList<>();
        sequence.add(this);

        return sequence;
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
    public String getFile(){
        return this.file;
    }

    @Override
    public void setFile(String file){
        this.file = file;
    }

    @Override
    public boolean matches(String name) {
        return this.getName().matches(name);
    }
}
