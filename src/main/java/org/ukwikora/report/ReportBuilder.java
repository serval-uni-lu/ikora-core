package org.ukwikora.report;

import org.ukwikora.model.Keyword;
import org.ukwikora.model.KeywordDefinition;
import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.model.Suite;
import org.ukwikora.model.TestCase;
import org.ukwikora.utils.Globals;

import java.time.LocalDateTime;
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
            ReportElement parent = stack.push(element);
            parent.addElement(element);
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
        report.setGenerated(LocalDateTime.now());
    }

    private Test createTestNode(TestCase testCase){
        Test test = new Test();

        test.setName(testCase.getName());
        test.setDocumentation(testCase.getDocumentation());
        test.setTags(testCase.getTags());

        return test;
    }

    private org.ukwikora.report.Keyword createKeywordNode(Keyword keyword){
        org.ukwikora.report.Keyword keywordNode = new org.ukwikora.report.Keyword();

        keywordNode.setName(keyword.getName());
        keywordNode.setDocumentation(keyword.getDocumentation());

        return keywordNode;
    }

    private ReportElement createSuiteNode(Suite suite) {
        org.ukwikora.report.Suite suiteNode = new org.ukwikora.report.Suite();

        suiteNode.setName(suite.getName());
        suiteNode.setDocumentation(suite.getDocumentation());
        suiteNode.setSource(suite.getSource().getAbsolutePath());

        return suiteNode;
    }
}
