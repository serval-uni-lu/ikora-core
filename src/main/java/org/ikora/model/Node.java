package org.ikora.model;

import org.ikora.analytics.visitor.NodeVisitor;
import org.ikora.analytics.visitor.VisitorMemory;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.runner.Runtime;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public abstract class Node implements Differentiable {
    private SourceFile sourceFile;
    private final Set<Node> dependencies;
    private Position position;

    Node(){
        dependencies = new HashSet<>();
    }

    public void setSourceFile(SourceFile sourceFile) {
        this.sourceFile = sourceFile;
    }

    public SourceFile getSourceFile() {
        return sourceFile;
    }

    public File getFile(){
        if(sourceFile == null){
            return null;
        }

        return sourceFile.getFile();
    }

    public String getFileName() {
        if(this.sourceFile == null){
            return "<NONE>";
        }

        return this.sourceFile.getName();
    }

    public String getLibraryName(){
        if(this.sourceFile == null){
            return "";
        }

        return this.sourceFile.getLibraryName();
    }

    public long getEpoch() {
        return sourceFile.getEpoch();
    }

    public Project getProject(){
        if(getSourceFile() == null){
            return null;
        }

        return getSourceFile().getProject();
    }

    public void addDependency(Node node) throws InvalidDependencyException {
        if(node == null) {
            throw new InvalidDependencyException("Cannot add null dependency");
        }

        this.dependencies.add(node);
    }

    public Set<Node> getDependencies() {
        return dependencies;
    }

    public boolean isDeadCode(){
        return getDependencies().size() == 0;
    }

    public void setPosition(Position position){
        this.position = position;
    }

    public Position getPosition(){
        return this.position;
    }

    public int getLoc() {
        int startLine = getPosition().getStartMark().getLine();
        int endLine = getPosition().getEndMark().getLine();

        return this.sourceFile.getLinesOfCode(startLine, endLine);
    }

    public abstract Value getNameAsValue();
    public abstract boolean matches(Token name);
    public abstract void accept(NodeVisitor visitor, VisitorMemory memory);
    public abstract void execute(Runtime runtime) throws Exception;
    public abstract Token getName();
}
