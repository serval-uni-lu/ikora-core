package org.ikora.model;

import org.ikora.analytics.Action;
import org.ikora.analytics.visitor.NodeVisitor;
import org.ikora.analytics.visitor.VisitorMemory;
import org.ikora.runner.Runtime;

import java.util.Collections;
import java.util.List;

public class TimeOut extends Node {
    private final Token name;
    private final Value variable;
    private final TimeValue value;
    private final boolean isNone;

    private Token errorMessage;

    public TimeOut(Token name, Token errorMessage){
        this.name = name;
        this.errorMessage = errorMessage;

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
        else if (this.name.getText().equalsIgnoreCase("NONE")){
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
        return new TimeOut(Token.empty(), Token.empty());
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
    public boolean matches(Token name) {
        return this.name.equalsValue(name);
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
    public Token getName() {
        return name;
    }

    @Override
    public void execute(Runtime runtime) throws Exception {
        //TODO: implement
    }
}
