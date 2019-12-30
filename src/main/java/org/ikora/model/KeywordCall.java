package org.ikora.model;

import org.ikora.analytics.Action;
import org.ikora.analytics.visitor.NodeVisitor;
import org.ikora.analytics.visitor.VisitorMemory;
import org.ikora.builder.Linker;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.exception.InvalidImportTypeException;
import org.ikora.runner.Runtime;
import org.ikora.utils.LevenshteinDistance;

import java.util.*;

public class KeywordCall extends Step {
    private Link<KeywordCall, Keyword> link;
    private List<Argument> argumentList;
    private List<Value> returnValues;

    public KeywordCall(String name) {
        super(name);
        this.argumentList = new ArrayList<>();
        this.link = new Link<>(this);
    }

    public void linkKeyword(Keyword keyword, Link.Import importLink)
            throws InvalidImportTypeException, InvalidDependencyException {
        link.addNode(keyword, importLink);
    }

    public void addArgument(Argument argument) {
        argument.setSourceFile(this.getSourceFile());
        this.argumentList.add(argument);
    }

    public void clearArguments() {
        this.argumentList.clear();
    }

    @Override
    public void setSourceFile(SourceFile sourceFile) {
        super.setSourceFile(sourceFile);

        for(Argument argument: this.argumentList){
            argument.setSourceFile(sourceFile);
        }
    }

    @Override
    public List<Argument> getArgumentList() {
        return this.argumentList;
    }

    @Override
    public boolean hasParameters() {
        return !this.argumentList.isEmpty();
    }

    public Optional<Keyword> getKeyword() {
        return link.getNode();
    }

    public Set<Keyword> getAllPotentialKeywords(Link.Import importType){
        return link.getAllLinks(importType);
    }

    @Override
    public boolean equals(Object other) {
        if(other == null){
            return false;
        }

        if (!(other instanceof KeywordCall)){
            return false;
        }

        if(!super.equals(other)) {
            return false;
        }

        KeywordCall call = (KeywordCall)other;

        boolean same = this.argumentList.size() == call.argumentList.size();

        for(int i = 0; same && i < this.argumentList.size(); ++i) {
            same &= this.argumentList.get(i).equals(call.argumentList.get(i));
        }

        return  same;
    }

    @Override
    public void execute(Runtime runtime) throws Exception{
        runtime.enterNode(this);
        Linker.link(this, runtime);

        Optional<Keyword> callee = link.getNode();

        if(callee.isPresent()){
            callee.get().execute(runtime);
            returnValues = runtime.getReturnValues();
        }
        else{
            throw new Exception("Need to have a better exception");
        }

        runtime.exitNode(this);
    }

    @Override
    public double distance(Differentiable other) {
        if(!(other instanceof KeywordCall)){
            return 1;
        }

        KeywordCall call = (KeywordCall)other;

        Optional<Keyword> thisCallee = this.getKeyword();
        Optional<Keyword> otherCallee = call.getKeyword();

        if(thisCallee.isPresent() && otherCallee.isPresent()){
            return thisCallee.get() == otherCallee.get() ? 0 : 1;
        }

        return this.getName().equalsIgnoreCase(call.getName()) ? 0 : 1;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(!(other instanceof Step)){
            return actions;
        }

        if(this.getClass() != other.getClass()){
            actions.add(Action.changeStepType(this, other));
        }
        else{
            KeywordCall call = (KeywordCall)other;

            if(!this.getName().equalsIgnoreCase(call.getName())){
                actions.add(Action.changeStepName(this, call));
            }

            if(LevenshteinDistance.index(this.getArgumentList(), call.getArgumentList()) > 0){
                actions.add(Action.changeStepArguments(this, call));
            }
        }

        return actions;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        builder.append(getName());

        for (Argument argument: argumentList){
            builder.append("\t");
            builder.append(argument.toString());
        }

        return builder.toString();
    }

    public List<Value> getReturnValues() {
        return this.returnValues;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public Optional<KeywordCall> getKeywordCall() {
        return Optional.of(this);
    }
}
