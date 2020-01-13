package org.ikora.model;

import org.ikora.analytics.Action;
import org.ikora.analytics.visitor.NodeVisitor;
import org.ikora.analytics.visitor.VisitorMemory;
import org.ikora.runner.Runtime;

import java.util.Collections;
import java.util.List;

public class TimeOut extends Node {
    private final Value variable;
    private final TimeValue value;
    private final boolean isNone;

    private String name;

    public TimeOut(String name){
        this.name = name;

        if(Value.isVariable(this.name)){
            this.variable = new Value(this, this.name);
            this.value = null;
            this.isNone = false;
        }
        else if(TimeValue.isValid(this.name)){
            this.variable = null;
            this.value = new TimeValue(this.name);
            this.isNone = false;
        }
        else if (this.name.equalsIgnoreCase("NONE")){
            this.variable = null;
            this.value = null;
            this.isNone = true;
        }
        else{
            this.variable = null;
            this.value = null;
            this.isNone = false;
        }
    }

    public static TimeOut none() {
        return new TimeOut("NONE");
    }

    public boolean isValid(){
        return this.variable != null || this.value != null || this.isNone;
    }

    @Override
    public Value getNameAsValue() {
        return new Value(name);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public boolean matches(String name) {
        return this.name.equalsIgnoreCase(name);
    }

    @Override
    public double distance(Differentiable other) {
        return 0;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void execute(Runtime runtime) throws Exception {
        //TODO: implement
    }
}
