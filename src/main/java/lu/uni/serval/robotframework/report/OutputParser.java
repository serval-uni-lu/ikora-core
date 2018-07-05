package lu.uni.serval.robotframework.report;

import lu.uni.serval.analytics.ReportAnalyzer;
import lu.uni.serval.robotframework.model.GitRepository;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutputParser {
    private static final DocumentBuilderFactory factory;

    static {
        factory = DocumentBuilderFactory.newInstance();
    }

    static public ReportAnalyzer parse(String reportFolderPath, String gitUrl) throws Exception {
        List<File> xmlFiles = getXmlPaths(reportFolderPath);

        GitRepository gitRepository = new GitRepository(gitUrl);
        ReportAnalyzer reports = new ReportAnalyzer();

        for(File xmlFile: xmlFiles){
            Report report = parseXml(xmlFile, gitRepository);

            if(report == null){
                continue;
            }

            reports.add(report);
        }

        return reports;
    }

    private static List<File> getXmlPaths(String folderPath){
        List<File> xmlFiles = new ArrayList<>();

        File folder = new File(folderPath);

        if(!folder.isDirectory()){
            throw new IllegalArgumentException("Expecting a folder path, got '" + folderPath + "' instead");
        }

        getXmlList(folder, xmlFiles);

        return xmlFiles;
    }

    private static void getXmlList(File directory, List<File> files) {
        File[] fileList = directory.listFiles();
        for (File file : fileList != null ? fileList : new File[0]) {
            if (file.isFile() && FilenameUtils.getExtension(file.getAbsolutePath()).equalsIgnoreCase("xml")) {
                files.add(file);
            } else if (file.isDirectory()) {
                getXmlList(file, files);
            }
        }
    }

    private static Report parseXml(File xmlFile, GitRepository gitRepository) throws Exception {
        Report report;

        ReportFactory reportFactory = new ReportFactory(gitRepository);

        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document = builder.parse(xmlFile);

            final Element root = document.getDocumentElement();

            if(!root.getTagName().equals("robot")){
                throw new IllegalArgumentException("XML root node should be root, got " + root.getTagName() + " instead");
            }

            report = reportFactory.create(root);

        } catch (IllegalArgumentException | ParserConfigurationException | SAXException | IOException e) {
            System.out.println(e.getMessage());
            report = null;
        }

        return report;
    }

}
