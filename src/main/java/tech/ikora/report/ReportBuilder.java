package tech.ikora.report;

import tech.ikora.model.*;
import tech.ikora.utils.Globals;
import tech.ikora.model.Keyword;
import tech.ikora.model.Suite;

import java.time.Instant;

import java.util.Date;
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

    public void enterSuite(tech.ikora.model.Suite suite) throws Exception {
        ReportElement element = createSuiteNode(suite);
        stack.peek().addElement(element);
        stack.push(element);
    }

    public void exitSuite(tech.ikora.model.Suite suite) {
        stack.pop();
    }

    public void enterNode(Node node) throws Exception {
        ReportElement element = null;

        if(TestCase.class.isAssignableFrom(node.getClass())){
            element = createTestNode((TestCase) node);
        }
        else if(tech.ikora.model.Keyword.class.isAssignableFrom(node.getClass())){
            element = createKeywordNode((tech.ikora.model.Keyword) node);
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
        report.setGenerated(Date.from(Instant.now()));
    }

    private Test createTestNode(TestCase testCase){
        Test test = new Test();

        test.setName(testCase.getName());
        test.setDocumentation(testCase.getDocumentation().toString());
        test.setTags(testCase.getTags().stream().map(Value::getName).collect(Collectors.toSet()));

        return test;
    }

    private tech.ikora.report.Keyword createKeywordNode(Keyword keyword){
        tech.ikora.report.Keyword keywordNode = new tech.ikora.report.Keyword();

        keywordNode.setName(keyword.getName());
        keywordNode.setDocumentation(keyword.getDocumentation().toString());

        return keywordNode;
    }

    private ReportElement createSuiteNode(Suite suite) {
        tech.ikora.report.Suite suiteNode = new tech.ikora.report.Suite();

        suiteNode.setName(suite.getName());
        suiteNode.setDocumentation(suite.getDocumentation());
        suiteNode.setSource(suite.getSource().getAbsolutePath());

        return suiteNode;
    }
}
