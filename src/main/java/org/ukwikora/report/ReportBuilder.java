package org.ukwikora.report;

import org.ukwikora.model.Keyword;
import org.ukwikora.model.KeywordDefinition;
import org.ukwikora.model.LibraryKeyword;
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

    public void enterKeyword(Keyword keyword) throws Exception {
        if(TestCase.class.isAssignableFrom(keyword.getClass())){
            stack.peek().addElement(createTestNode((TestCase)keyword));
        }
        else if(KeywordDefinition.class.isAssignableFrom(keyword.getClass())
        || LibraryKeyword.class.isAssignableFrom(keyword.getClass())){
            stack.peek().addElement(createKeywordNode(keyword));
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
}
