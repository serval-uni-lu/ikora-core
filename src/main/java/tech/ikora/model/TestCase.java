package tech.ikora.model;

import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.exception.InvalidTypeException;
import tech.ikora.types.BaseTypeList;

import java.util.Collections;
import java.util.List;

public class TestCase extends KeywordDefinition {
    private KeywordCall setup = null;
    private KeywordCall tearDown = null;
    private KeywordCall template = null;

    public TestCase(Token name) {
        super(name);
    }

    public void setSetup(KeywordCall setup){
        this.setup = setup;
        this.addAstChild(setup);
    }

    public void setSetup(Step step) throws InvalidTypeException {
        setSetup(step.toCall());
    }

    public void setTearDown(KeywordCall tearDown){
        this.tearDown = tearDown;
        this.addAstChild(tearDown);
    }

    public void setTearDown(Step tearDown) throws InvalidTypeException {
        setTearDown(tearDown.toCall());
    }

    public void setTemplate(KeywordCall template){
        this.template = template;
        this.addAstChild(template);
    }

    public void setTemplate(Step template) throws InvalidTypeException {
        setTemplate(template.toCall());
    }

    public KeywordCall getSetup(){
        return setup;
    }

    public KeywordCall getTearDown(){
        return tearDown;
    }

    public KeywordCall getTemplate() {
        return template;
    }

    public boolean hasTemplate(){
        return template != null;
    }

    @Override
    public boolean isDeadCode(){
        return false;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public ArgumentList getReturnValues() {
        return new ArgumentList();
    }

    @Override
    public BaseTypeList getArgumentTypes() {
        return new BaseTypeList();
    }
}
