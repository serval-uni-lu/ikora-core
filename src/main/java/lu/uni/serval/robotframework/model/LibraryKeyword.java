package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.tree.LabelTreeNode;

import java.util.HashSet;
import java.util.Set;


public abstract class LibraryKeyword implements Keyword {
    private Set<Keyword> dependencies;
    private LabelTreeNode node;

    public LibraryKeyword() {
        this.dependencies = new HashSet<>();
    }

    @Override
    public Keyword getStep(int position) {
        return null;
    }

    @Override
    public LabelTreeNode getNode() {
        return node;
    }

    @Override
    public Set<Keyword> getDependencies() {
        return dependencies;
    }

    @Override
    public void addDependency(Keyword keyword) {
        this.dependencies.add(keyword);
    }

    public static String toKeyword(Class<? extends LibraryKeyword> libraryClass) {
        String name = libraryClass.getSimpleName();
        return name.replaceAll("([A-Z])", " $1").trim().toLowerCase();
    }
}
