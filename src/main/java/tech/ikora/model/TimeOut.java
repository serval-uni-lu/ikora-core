package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.ValueLinker;
import tech.ikora.exception.MalformedVariableException;
import tech.ikora.runner.Runtime;

import java.util.Collections;
import java.util.List;

public class TimeOut extends SourceNode {
    private final Token name;
    private final Token errorMessage;

    private final Variable variable;
    private final TimeValue value;
    private final boolean isNone;

    public TimeOut(Token name, Token errorMessage) throws MalformedVariableException {
        addToken(name);
        addToken(errorMessage);

        this.name = name;
        this.errorMessage = errorMessage;

        if(ValueLinker.isVariable(this.name)){
            this.variable = Variable.create(this.name);
            this.addAstChild(this.variable);
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

    private TimeOut(){
        this.name = Token.empty();
        this.errorMessage = Token.empty();
        this.variable = null;
        this.value = null;
        this.isNone = false;
    }

    public static TimeOut none() {
        return new TimeOut();
    }

    public boolean isValid(){
        return this.variable != null || this.value != null || this.isNone;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public boolean matches(Token name) {
        return this.name.equalsIgnorePosition(name);
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
    public Token getNameToken() {
        return name;
    }

    @Override
    public void execute(Runtime runtime) throws Exception {
        //TODO: implement
    }
}
