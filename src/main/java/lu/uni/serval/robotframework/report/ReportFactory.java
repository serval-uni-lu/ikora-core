package lu.uni.serval.robotframework.report;

import lu.uni.serval.robotframework.model.*;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public class ReportFactory {
    private final GitRepository gitRepository;

    public ReportFactory(GitRepository gitRepository){
        this.gitRepository = gitRepository;
    }

    public Report create(final Element robotElement) throws Exception {
        Report report = new Report();

        String generated = robotElement.getAttribute("generated");
        String generator = robotElement.getAttribute("generator");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss.SSS");
        LocalDateTime dateTime = LocalDateTime.parse(generated, formatter);

        report.setCreationTime(dateTime);
        report.setGenerator(generator);

        HashSet<String> types = new HashSet<>(Collections.singletonList("suite"));

        for(Element suiteElement: getChildren(robotElement, types)){
            Suite suite = parseSuite(suiteElement);

            if(suite == null){
                continue;
            }

            report.addSuite(suite);
        }

        return report;
    }

    private Suite parseSuite(final Element suiteElement) throws Exception {
        Suite suite = new Suite();
        suite.setName(suiteElement.getAttribute("name"));
        suite.setId(suiteElement.getAttribute("id"));
        suite.setSource(suiteElement.getAttribute("source"));

        HashSet<String> types = new HashSet<>(Arrays.asList("suite", "kw", "test"));

        for(Element child: getChildren(suiteElement, types)){
            String tagName = child.getTagName();
            if(tagName.equalsIgnoreCase("suite")){
                Suite subSuite = parseSuite(child);

                if(subSuite == null){
                    continue;
                }

                suite.addSuite(subSuite);
            }
            else if(tagName.equalsIgnoreCase("test")){
                KeywordStatus keyword = parseTestCase(suite, child);

                if(keyword == null){
                    continue;
                }

                suite.addKeyword(keyword);
            }
        }

        return suite;
    }

    private KeywordStatus parseTestCase(final Suite suite, final Element keywordElement) throws Exception {
        KeywordStatus keywordStatus = new KeywordStatus();
        TestCase testCase = findTestCase(suite, keywordElement);

        if(testCase == null) {
            throw new Exception("could not find test case from report");
        }

        keywordStatus.setKeyword(testCase);

        processTree(keywordStatus, keywordElement);

        return keywordStatus;
    }

    private TestCase findTestCase(Suite suite, Element keywordElement) {
        return gitRepository.findTestCase(suite.getSource(), keywordElement.getAttribute("name"));
    }

    private KeywordStatus parseKeyword(KeywordStatus parent, final Element keywordElement) throws Exception {
        KeywordStatus keywordStatus = new KeywordStatus();
        Step step = findStep(parent, keywordElement);

        if(step == null) {
            throw new Exception ("could not find step from report");
        }

        keywordStatus.setKeyword(step);

        processTree(keywordStatus, keywordElement);

        return keywordStatus;
    }

    private Step findStep(KeywordStatus parent, Element keywordElement) throws Exception {
        if(!(parent.getKeyword() instanceof KeywordDefinition)) {
            throw new Exception("Was waiting for a keyword definition");
        }

        KeywordDefinition definition = (KeywordDefinition)parent.getKeyword();

        for(Step step: definition){
            if(step.getName().toString().equalsIgnoreCase(keywordElement.getAttribute("name"))){
                return step;
            }
        }

        return null;
    }

    private void processTree(KeywordStatus keywordStatus, final Element keywordElement) throws Exception {

        HashSet<String> types = new HashSet<>(Arrays.asList("kw", "arguments", "status", "msg"));

        for(Element childElement: getChildren(keywordElement, types)){
            String elementName = childElement.getTagName();

            if(elementName.equalsIgnoreCase("kw")){
                KeywordStatus child = parseKeyword(keywordStatus, childElement);

                if(child == null){
                    continue;
                }

                keywordStatus.addChild(child);
            }
            else if(elementName.equalsIgnoreCase("status")){
                keywordStatus.setStatus(childElement.getAttribute("status"));
            }
            else if(elementName.equalsIgnoreCase("msg")){
                keywordStatus.setLog(childElement.getTextContent());
            }
            else{
                System.out.println("Ignored tag '" + elementName + "' while parsing kw");
            }
        }
    }

    private String getType(Element keywordElement) {
        if(keywordElement.getTagName().equalsIgnoreCase("test")){
            return "test";
        }

        return keywordElement.getAttribute("type");
    }

    private List<String> parseArguments(Element argumentsElement) {
        List<String> arguments = new ArrayList<>();

        HashSet<String> types = new HashSet<>(Collections.singletonList("arg"));

        for(Element argumentElement: getChildren(argumentsElement, types)){
           arguments.add(argumentElement.getTextContent());
        }

        return arguments;
    }

    private static List<Element> getChildren(Element parent, Set<String> types){
        NodeList children = parent.getChildNodes();
        int childrenLength = children.getLength();

        List<Element> elements = new ArrayList<>(childrenLength);

        for(int childIndex = 0; childIndex < childrenLength; ++childIndex){
            Node node = children.item(childIndex);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                final Element child = (Element)node;
                if(types.contains(child.getTagName().toLowerCase())){
                    elements.add(child);
                }
            }
        }

        return elements;
    }
}
