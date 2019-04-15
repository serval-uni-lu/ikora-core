package org.ukwikora.model;

import org.ukwikora.analytics.Action;
import org.ukwikora.analytics.VisitorMemory;
import org.ukwikora.exception.InvalidDependencyException;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TimeOut implements Statement {
    private final Variable variable;
    private final TimeValue value;

    private TestCaseFile file;
    private LineRange lineRange;
    private String name;
    private KeywordDefinition parent;

    public TimeOut(String name, Variable variable){
        this.name = name;
        this.variable = variable;
        this.value = null;

        if(this.variable != null){
            this.variable.addDependency(this);
        }
    }

    public TimeOut(String name, TimeValue value){
        this.name = name;
        this.variable = null;
        this.value = value;
    }

    public KeywordDefinition getParent() {
        return parent;
    }

    public void setParent(KeywordDefinition parent) {
        this.parent = parent;
    }

    @Override
    public void setFile(@Nonnull TestCaseFile file) {
        this.file = file;
    }

    @Override
    public TestCaseFile getFile() {
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
    public void accept(StatementVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public boolean matches(@Nonnull String name) {
        return this.name.equalsIgnoreCase(name);
    }

    @Override
    public Set<Statement> getDependencies() {
        if(parent == null){
            return Collections.emptySet();
        }

        return Collections.singleton(parent);
    }

    @Override
    public void addDependency(@Nonnull Statement dependency) throws InvalidDependencyException {
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
