package org.ikora.model;

import org.ikora.analytics.visitor.NodeVisitor;
import org.ikora.analytics.visitor.VisitorMemory;
import org.ikora.exception.InvalidTypeException;

import java.util.Collections;
import java.util.List;

public class TestCase extends KeywordDefinition {
    private KeywordCall setup;
    private KeywordCall tearDown;
    private KeywordCall template;

    public void setSetup(KeywordCall setup){
        this.setup = setup;
        this.setup.setSourceFile(this.getSourceFile());
    }

    public void setSetup(Step step) throws InvalidTypeException {
        setSetup(toCall(step));
    }

    public void setTearDown(KeywordCall tearDown){
        this.tearDown = tearDown;
        this.tearDown.setSourceFile(this.getSourceFile());
    }

    public void setTearDown(Step tearDown) throws InvalidTypeException {
        setTearDown(toCall(tearDown));
    }

    public void setTemplate(KeywordCall template){
        this.template = template;
        this.template.setSourceFile(this.getSourceFile());
    }

    public void setTemplate(Step template) throws InvalidTypeException {
        setTemplate(toCall(template));
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
    public void setSourceFile(SourceFile sourceFile) {
        super.setSourceFile(sourceFile);

        if(this.setup != null){
            this.setup.setSourceFile(sourceFile);
        }

        if(this.tearDown != null){
            this.tearDown.setSourceFile(sourceFile);
        }

        if(this.template != null){
            this.template.setSourceFile(sourceFile);
        }
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
    public List<Value> getReturnValues() {
        return Collections.emptyList();
    }

    @Override
    public int getMaxNumberArguments() {
        return 0;
    }
}
