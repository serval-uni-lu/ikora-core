package lu.uni.serval.robotframework.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class LibraryKeyword implements Keyword {
    private Set<Keyword> dependencies;

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
}
