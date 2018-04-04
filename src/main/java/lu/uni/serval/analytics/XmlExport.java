package lu.uni.serval.analytics;

import lu.uni.serval.utils.CompareCache;
import lu.uni.serval.utils.KeywordData;
import lu.uni.serval.utils.UnorderedPair;
import lu.uni.serval.utils.tree.TreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.Map;

public class XmlExport {
    public static void write(StatisticsResults statisticsResults, CloneResults cloneResults, String filePath) throws IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        File file = new File(filePath);

        if(!file.exists()){
            if(!file.createNewFile()){
                throw new InvalidPathException(filePath, "failed to create file for this path");
            }
        }

        if(!file.canWrite()){
            throw new InvalidPathException(filePath, "not a valid file path");
        }

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.newDocument();

            Element root = dom.createElement("analytics");

            if(statisticsResults != null){
                Element statistics = createStatistics(statisticsResults, dom);
                root.appendChild(statistics);
            }

            if(cloneResults != null){
                Element clones = createClones(cloneResults, dom);
                root.appendChild(clones);
            }

            dom.appendChild(root);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                // send DOM to file
                tr.transform(new DOMSource(dom),
                        new StreamResult(new FileOutputStream(filePath)));

            } catch (TransformerException | IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }
    }

    private static Element createStatistics(StatisticsResults statisticsResults, Document dom){
        Element statistics = dom.createElement("statistics");

        for(Map.Entry<String, Integer> integerStatistic: statisticsResults.getIntegerStatistics().entrySet()){
            Element statistic = dom.createElement(integerStatistic.getKey());
            statistic.setAttribute("value", String.valueOf(integerStatistic.getValue()));
            statistics.appendChild(statistic);
        }

        for(Map.Entry<String, Double> doubleStatistic: statisticsResults.getDoubleStatistics().entrySet()){
            Element statistic = dom.createElement(doubleStatistic.getKey());
            statistic.setAttribute("value", String.valueOf(doubleStatistic.getValue()));
            statistics.appendChild(statistic);
        }

        return statistics;
    }

    private static Element createClones(CloneResults cloneResults, Document dom) {
        Element clones = dom.createElement("clones");

        Element same = writeClones(dom, cloneResults, CloneResults.CloneType.Same);
        clones.appendChild(same);

        Element synonyms = writeClones(dom, cloneResults, CloneResults.CloneType.Synonym);
        clones.appendChild(synonyms);

        Element homonyms = writeClones(dom, cloneResults, CloneResults.CloneType.Homonym);
        clones.appendChild(homonyms);

        return clones;
    }

    private static Element writeClones(Document dom, CloneResults results, CloneResults.CloneType type){
        Element synonymsNode = dom.createElement(getParentTag(type));

        final CompareCache<TreeNode, CloneIndex> clones = results.getByType(type);
        for(Map.Entry<UnorderedPair<TreeNode>, CloneIndex> clone: clones){
            Element synonymNode = dom.createElement(getElementTag(type));
            synonymNode.setAttribute("keywordIndex", String.valueOf(clone.getValue().getKeywordRatio()));
            synonymNode.setAttribute("treeIndex", String.valueOf(clone.getValue().getTreeRatio()));
            synonymNode.setAttribute("semanticIndex", String.valueOf(clone.getValue().getSemanticRatio()));

            Element keyword1Node = writeKeyword(dom, clone.getKey().first());
            synonymNode.appendChild(keyword1Node);

            Element keyword2Node = writeKeyword(dom, clone.getKey().second());
            synonymNode.appendChild(keyword2Node);

            synonymsNode.appendChild(synonymNode);
        }

        return synonymsNode;
    }

    private static String getParentTag(CloneResults.CloneType type){
        switch (type){
            case Same: return "sames";
            case Homonym: return "homonyms";
            case Synonym: return "synonyms";
        }

        throw new NotImplementedException();
    }

    private static String getElementTag(CloneResults.CloneType type){
        switch (type){
            case Same: return "same";
            case Homonym: return "homonym";
            case Synonym: return "synonym";
        }

        throw new NotImplementedException();
    }

    private static Element writeKeyword(Document dom, TreeNode keyword){
        Element keywordNode = dom.createElement("keyword");
        keywordNode.setAttribute("name", ((KeywordData)keyword.data).name);
        keywordNode.setAttribute("file", ((KeywordData)keyword.data).file);

        return keywordNode;
    }
}
