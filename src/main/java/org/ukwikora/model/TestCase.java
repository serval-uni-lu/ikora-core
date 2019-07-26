package org.ukwikora.model;

import org.apache.commons.io.FilenameUtils;
import org.ukwikora.analytics.VisitorMemory;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class TestCase extends KeywordDefinition {
    private KeywordCall setup;
    private KeywordCall tearDown;
    private List<String> suites;

    public void setSetup(KeywordCall setup){
        this.setup = setup;
    }

    public void setSetup(Step step){
        setSetup(toCall(step));
    }

    public void setTearDown(KeywordCall tearDown){
        this.tearDown = tearDown;
    }

    public void setTearDown(Step tearDown){
        setTearDown(toCall(tearDown));
    }

    public KeywordCall getSetup(){
        return setup;
    }

    public KeywordCall getTearDown(){
        return tearDown;
    }

    public List<String> getSuites(){
        return suites;
    }

    @Override
    public void setFile(@Nonnull TestCaseFile file) {
        super.setFile(file);

        String suiteString = FilenameUtils.removeExtension(file.getName());
        suites = Arrays.asList(suiteString.split("[\\\\/]"));
    }

    @Override
    public void accept(StatementVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }
}
