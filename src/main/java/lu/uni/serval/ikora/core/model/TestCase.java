package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.exception.InvalidTypeException;
import lu.uni.serval.ikora.core.types.BaseTypeList;

import java.util.Optional;

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

    public Optional<KeywordCall> getSetup(){
        return Optional.ofNullable(setup);
    }

    public Optional<KeywordCall> getTearDown(){
        return Optional.ofNullable(tearDown);
    }

    public Optional<KeywordCall> getTemplate() {
        return Optional.ofNullable(template);
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
    public NodeList<Value> getReturnValues() {
        return new NodeList<>();
    }

    @Override
    public BaseTypeList getArgumentTypes() {
        return new BaseTypeList();
    }
}
