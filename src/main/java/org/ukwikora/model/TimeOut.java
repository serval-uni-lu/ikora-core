package org.ukwikora.model;

import org.ukwikora.analytics.Action;
import org.ukwikora.analytics.NodeVisitor;
import org.ukwikora.analytics.VisitorMemory;
import org.ukwikora.exception.InvalidDependencyException;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TimeOut implements Node {
    private final Value variable;
    private final TimeValue value;

    private SourceFile file;
    private LineRange lineRange;
    private String name;
    private KeywordDefinition parent;

    public TimeOut(String name){
        this.name = name;

        if(Value.isVariable(this.name)){
            this.variable = new Value(this, this.name);
            this.value = null;
        }
        else if(TimeValue.isValid(this.name)){
            this.variable = null;
            this.value = new TimeValue(this.name);
        }
        else{
            this.variable = null;
            this.value = null;
        }
    }

    public boolean isValid(){
        return this.variable != null || this.value != null;
    }

    public KeywordDefinition getParent() {
        return parent;
    }

    public void setParent(KeywordDefinition parent) {
        this.parent = parent;
    }

    @Override
    public void setFile(@Nonnull SourceFile file) {
        this.file = file;
    }

    @Override
    public SourceFile getFile() {
        return file;
    }

    @Override
    public String getFileName() {
        if(this.file == null){
            return "";
        }

        return this.file.getName();
    }

    @Override
    public String getLibraryName() {
        if(this.file == null){
            return "";
        }

        return this.file.getLibraryName();
    }

    @Override
    public void setLineRange(@Nonnull LineRange lineRange) {
        this.lineRange = lineRange;
    }

    @Override
    public LineRange getLineRange() {
        return lineRange;
    }

    @Override
    public int getLoc() {
        return this.file.getLoc(this.lineRange);
    }

    @Override
    public long getEpoch() {
        return file.getEpoch();
    }

    @Override
    public Value getNameAsValue() {
        return new Value(name);
    }

    @Override
    public boolean isDeadCode(){
        return false;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public boolean matches(@Nonnull String name) {
        return this.name.equalsIgnoreCase(name);
    }

    @Override
    public Set<Node> getDependencies() {
        if(parent == null){
            return Collections.emptySet();
        }

        return Collections.singleton(parent);
    }

    @Override
    public void addDependency(@Nonnull Node dependency) throws InvalidDependencyException {
        throw new InvalidDependencyException();
    }

    @Override
    public double distance(@Nonnull Differentiable other) {
        return 0;
    }

    @Override
    public List<Action> differences(@Nonnull Differentiable other) {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return name;
    }
}
