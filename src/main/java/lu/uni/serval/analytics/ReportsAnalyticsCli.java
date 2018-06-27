package lu.uni.serval.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import lu.uni.serval.robotframework.report.OutputParser;
import lu.uni.serval.robotframework.report.Report;
import lu.uni.serval.utils.CommandRunner;
import lu.uni.serval.utils.Configuration;
import lu.uni.serval.utils.Plugin;
import lu.uni.serval.utils.exception.DuplicateNodeException;
import lu.uni.serval.utils.tree.LabelTreeNode;

import java.io.*;

public class ReportsAnalyticsCli implements CommandRunner {
    @Override
    public void run() throws Exception {
        Configuration config = Configuration.getInstance();
        Plugin analytics = config.getPlugin("report analytics");

        String reportLocation = (String)analytics.getAdditionalProperty("report location", "");
        String gitUrl = (String)analytics.getAdditionalProperty("git url", "");
        String localFolder = (String)analytics.getAdditionalProperty("git local path", "");

        ReportAnalyzer reports = OutputParser.parse(reportLocation, gitUrl, localFolder);

/*
        for(Report report: reports){
            PrintWriter writer = null;
            try {
                writer = new PrintWriter("C:\\Users\\renaud.rwemalika\\Desktop\\report_" + report.getCreationTime().toLocalDate().toString() + ".txt", "UTF-8");
                for(LabelTreeNode keyword: report.getKeywords()){
                    writer.println(keyword);
                }

            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            } finally {
                if(writer != null){
                    writer.close();
                }
            }
        }
*/

        exportDifferences(analytics, reports);
        exportStatusSequences(analytics, reports);
    }

    private void exportStatusSequences(Plugin analytics, ReportAnalyzer reports) {
        if(analytics.getAdditionalProperty("output status file", "").equals("")){
            return;
        }

        StatusResults status = reports.getStatus();

        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File((String)analytics.getAdditionalProperty("output status file", "./report-status.json"));

            mapper.writeValue(file, status);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportDifferences(Plugin analytics, ReportAnalyzer reports) {
        if(analytics.getAdditionalProperty("output differences file", "").equals("")){
            return;
        }

        DifferenceResults results = reports.findDifferences();

        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File((String)analytics.getAdditionalProperty("output differences file", "./report-differences.json"));

            mapper.writeValue(file, results);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
