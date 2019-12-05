package org.ikora.report;

import org.ikora.model.Keyword;
import org.ikora.model.KeywordDefinition;
import org.ikora.model.LibraryKeyword;
import org.ikora.model.Suite;
import org.ikora.model.TestCase;
import org.ikora.utils.Globals;

import java.time.Instant;

import java.util.Date;
import java.util.Optional;
import java.util.Stack;

public class ReportBuilder {
    private Report report;
    private Stack<ReportElement> stack;

    public ReportBuilder(){
        this.report = new Report();
    }

    public Optional<Report> getReport() {
        return Optional.ofNullable(report);
    }

    public void enterSuite(Suite suite) throws Exception {
        ReportElement element = createSuiteNode(suite);
        stack.peek().addElement(element);
        stack.push(element);
    }

    public void exitSuite(Suite suite) {
        stack.pop();
    }

    public void enterKeyword(Keyword keyword) throws Exception {
        ReportElement element = null;

        if(TestCase.class.isAssignableFrom(keyword.getClass())){
            element = createTestNode((TestCase)keyword);
        }
        else if(KeywordDefinition.class.isAssignableFrom(keyword.getClass())
        || LibraryKeyword.class.isAssignableFrom(keyword.getClass())){
            element =  createKeywordNode(keyword);
        }

        if(element != null){
            stack.peek().addElement(element);
            stack.push(element);
        }
    }

    public void exitKeyword(Keyword keyword) {
        stack.pop();
    }

    public void reset() {
        report = new Report();
        report.setGenerator(Globals.applicationCanonical);

        stack = new Stack<>();
        stack.push(report);
    }

    public void finish(){
        report.setGenerated(Date.from(Instant.now()));
    }

    private Test createTestNode(TestCase testCase){
        Test test = new Test();

        test.setName(testCase.getName());
        test.setDocumentation(testCase.getDocumentation());
        test.setTags(testCase.getTags());

        return test;
    }

    private org.ikora.report.Keyword createKeywordNode(Keyword keyword){
        org.ikora.report.Keyword keywordNode = new org.ikora.report.Keyword();

        keywordNode.setName(keyword.getName());
        keywordNode.setDocumentation(keyword.getDocumentation());

        return keywordNode;
    }

    private ReportElement createSuiteNode(Suite suite) {
        org.ikora.report.Suite suiteNode = new org.ikora.report.Suite();

        suiteNode.setName(suite.getName());
        suiteNode.setDocumentation(suite.getDocumentation());
        suiteNode.setSource(suite.getSource().getAbsolutePath());

        return suiteNode;
    }
}
