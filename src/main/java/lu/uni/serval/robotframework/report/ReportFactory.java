package lu.uni.serval.robotframework.report;

import lu.uni.serval.utils.KeywordData;
import lu.uni.serval.utils.tree.TreeNode;
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
    public static Report create(final Element robotElement){
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

    private static Suite parseSuite(final Element suiteElement){
        Suite suite = new Suite();
        suite.setName(suiteElement.getAttribute("name"));
        suite.setId(suiteElement.getAttribute("id"));
        suite.setSource(suiteElement.getAttribute("source"));

        HashSet<String> types = new HashSet<>(Arrays.asList("suite", "kw"));

        for(Element child: getChildren(suiteElement, types)){

            if(child.getTagName().equalsIgnoreCase("suite")){
                Suite subSuite = parseSuite(child);

                if(subSuite == null){
                    continue;
                }

                suite.addSuite(subSuite);
            }
            else if(child.getTagName().equalsIgnoreCase("kw")){
                TreeNode keyword = parseKeyword(child);

                if(keyword == null){
                    continue;
                }

                suite.addKeyword(keyword);
            }
        }

        return suite;
    }

    private static TreeNode parseKeyword(Element keywordElement) {
        KeywordData data = new KeywordData();
        data.type = keywordElement.getAttribute("type");
        data.name = keywordElement.getAttribute("name");
        data.library = keywordElement.getAttribute("library");

        TreeNode treeNode = new TreeNode(data, true);

        HashSet<String> types = new HashSet<>(Arrays.asList("doc", "kw", "arguments"));

        for(Element child: getChildren(keywordElement, types)){

            if(child.getTagName().equalsIgnoreCase("doc")){
                data.documentation = child.getTextContent();
            }
            else if(child.getTagName().equalsIgnoreCase("kw")){
                TreeNode keyword = parseKeyword(child);

                if(keyword == null){
                    continue;
                }

                treeNode.addChild(keyword);
            }
            else if(child.getTagName().equalsIgnoreCase("arguments")){
                data.arguments = parseArguments(child);
            }
            else if(child.getTagName().equalsIgnoreCase("status")){
                data.setStatus(child.getAttribute("status"));
            }
        }

        return treeNode;
    }

    private static List<String> parseArguments(Element argumentsElement) {
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
