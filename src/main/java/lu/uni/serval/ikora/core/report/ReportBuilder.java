package lu.uni.serval.ikora.core.report;

import lu.uni.serval.ikora.core.exception.BadElementException;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.model.Keyword;
import lu.uni.serval.ikora.core.model.Suite;
import lu.uni.serval.ikora.core.utils.Globals;

import java.time.Instant;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReportBuilder {
    private Report report;
    private Deque<ReportElement> stack;

    public ReportBuilder(){
        this.report = new Report();
    }

    public Optional<Report> getReport() {
        return Optional.ofNullable(report);
    }

    public void enterSuite(Suite suite) throws BadElementException {
            ReportElement element = createSuiteNode(suite);
            stack.peek().addElement(element);
            stack.push(element);
    }

    public void exitSuite(Suite suite) {
        stack.pop();
    }

    public void enterNode(Node node) throws BadElementException {
        ReportElement element = null;

        if(TestCase.class.isAssignableFrom(node.getClass())){
            element = createTestNode((TestCase) node);
        }
        else if(Keyword.class.isAssignableFrom(node.getClass())){
            element = createKeywordNode((Keyword) node);
        }

        if(element != null){
            stack.peek().addElement(element);
            stack.push(element);
        }
    }

    public void exitNode(Node node) {
        stack.pop();
    }

    public void reset() {
        report = new Report();
        report.setGenerator(Globals.APPLICATION_CANONICAL);

        stack = new LinkedList<>();
        stack.push(report);
    }

    public void finish(){
        report.setGenerated(Instant.now());
    }

    private TestNode createTestNode(TestCase testCase){
        TestNode test = new TestNode();

        test.setName(testCase.getName());
        test.setDocumentation(testCase.getDocumentation().toString());
        test.setTags(testCase.getTags().stream().map(Value::getName).collect(Collectors.toSet()));

        return test;
    }

    private KeywordNode createKeywordNode(Keyword keyword){
        KeywordNode keywordNode = new KeywordNode();

        keywordNode.setName(keyword.getName());
        keywordNode.setDocumentation(keyword.getDocumentation().toString());

        return keywordNode;
    }

    private ReportElement createSuiteNode(Suite suite) {
        SuiteNode suiteNode = new SuiteNode();

        suiteNode.setName(suite.getName());
        suiteNode.setDocumentation(suite.getDocumentation());
        suiteNode.setSource(suite.getSource().getAbsolutePath());

        return suiteNode;
    }
}
