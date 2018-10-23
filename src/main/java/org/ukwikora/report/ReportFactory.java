package org.ukwikora.report;

import org.apache.commons.io.FilenameUtils;
import org.ukwikora.model.GitRepository;
import org.ukwikora.model.Keyword;
import org.ukwikora.model.TestCase;
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

        linkReport(report);

        return report;
    }

    private Suite parseSuite(final Element suiteElement) {
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
                KeywordStatus keyword = parseKeyword(child);

                if(keyword == null){
                    continue;
                }

                suite.addKeyword(keyword);
            }
        }

        return suite;
    }

    private KeywordStatus parseKeyword(final Element keywordElement) {
        KeywordStatus keywordStatus = new KeywordStatus();
        keywordStatus.setName(keywordElement.getAttribute("name"));

        HashSet<String> types = new HashSet<>(Arrays.asList("kw", "arguments", "status", "msg"));

        for(Element childElement: getChildren(keywordElement, types)){
            String elementName = childElement.getTagName();

            if(elementName.equalsIgnoreCase("kw")){
                KeywordStatus child = parseKeyword(childElement);

                if(child == null){
                    continue;
                }

                keywordStatus.addChild(child);
            }
            else if(elementName.equalsIgnoreCase("status")){
                keywordStatus.setStatus(childElement.getAttribute("status"));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss.SSS");

                String startTime = childElement.getAttribute("starttime");
                keywordStatus.setStartTime(LocalDateTime.parse(startTime, formatter));

                String endTime = childElement.getAttribute("endtime");
                keywordStatus.setEndTime(LocalDateTime.parse(endTime, formatter));
            }
            else if(elementName.equalsIgnoreCase("msg")){
                keywordStatus.setLog(childElement.getTextContent());
            }
            else if(elementName.equalsIgnoreCase("arguments")){

                HashSet<String> arguments = new HashSet<>(Arrays.asList("argument"));
                for(Element child: getChildren(childElement, arguments)){
                    String argument = child.getTextContent();
                    keywordStatus.addArgument(argument);
                }
            }
            else{
                System.out.println("Ignored tag '" + elementName + "' while parsing kw");
            }
        }

        return keywordStatus;
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

    private void linkReport(Report report) throws Exception {
        gitRepository.checkout(report.getCreationTime());

        for(Suite suite: report.getSuites()){
            String suiteName = FilenameUtils.getBaseName(suite.getName());

            if(!suiteName.equalsIgnoreCase(gitRepository.getName())){
                continue;
            }

            linkSuite(suite);
        }
    }

    private void linkSuite(Suite suite) throws Exception {
        for (Suite child: suite.getChildren()){
            linkSuite(child);
        }

        for(KeywordStatus keywordStatus: suite.getKeywords()){
            linkTestCase(keywordStatus);
        }
    }

    private void linkTestCase(KeywordStatus keywordStatus) throws Exception {
        TestCase testCase = gitRepository.findTestCase(keywordStatus.getName());
        keywordStatus.setKeyword(testCase);

        for(KeywordStatus child: keywordStatus.getChildren()){
            linkKeyword(testCase, child);
        }
    }

    private void linkKeyword(Keyword parent, KeywordStatus status) throws Exception {
        if(parent == null){
            throw new Exception("parent keyword should not be null");
        }

        int position = status.getStepPosition(false);
        Keyword keyword = parent.getStep(position);

        status.setKeyword(keyword);

        if(keyword == null){
            return;
        }

        for(KeywordStatus child: status.getChildren()){
            linkKeyword(keyword, child);
        }
    }
}
