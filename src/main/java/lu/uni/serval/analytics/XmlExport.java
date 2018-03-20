package lu.uni.serval.analytics;

import lu.uni.serval.utils.CompareCache;
import lu.uni.serval.utils.KeywordData;
import lu.uni.serval.utils.UnorderedPair;
import lu.uni.serval.utils.tree.TreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class XmlExport {
    public static void write(CloneResults results, String path){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.newDocument();

            Element root = dom.createElement("analytics");

            Element synonyms = writeSynonyms(dom, results);
            root.appendChild(synonyms);

            dom.appendChild(root);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                // send DOM to file
                tr.transform(new DOMSource(dom),
                        new StreamResult(new FileOutputStream(path)));

            } catch (TransformerException te) {
                System.out.println(te.getMessage());
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }
    }

    private static Element writeSynonyms(Document dom, CloneResults results){
        Element synonymsNode = dom.createElement("synonyms");

        final CompareCache<TreeNode, CloneIndex> synonyms = results.getSynonym();
        for(Map.Entry<UnorderedPair<TreeNode>, CloneIndex> synonym: synonyms){
            Element synonymNode = dom.createElement("synonym");
            synonymNode.setAttribute("keywordIndex", String.valueOf(synonym.getValue().getKeywordRatio()));
            synonymNode.setAttribute("treeIndex", String.valueOf(synonym.getValue().getTreeRatio()));

            Element keyword1Node = writeKeyword(dom, synonym.getKey().first());
            synonymNode.appendChild(keyword1Node);

            Element keyword2Node = writeKeyword(dom, synonym.getKey().second());
            synonymNode.appendChild(keyword2Node);

            synonymsNode.appendChild(synonymNode);
        }

        return synonymsNode;
    }

    private static Element writeKeyword(Document dom, TreeNode keyword){
        Element keywordNode = dom.createElement("keyword");
        keywordNode.setAttribute("name", ((KeywordData)keyword.data).name);
        keywordNode.setAttribute("file", ((KeywordData)keyword.data).file);

        return keywordNode;
    }
}
