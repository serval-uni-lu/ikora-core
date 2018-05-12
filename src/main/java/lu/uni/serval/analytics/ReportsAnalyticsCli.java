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
    public void run() throws DuplicateNodeException {
        Configuration config = Configuration.getInstance();
        Plugin analytics = config.getPlugin("report analytics");
        String location = (String)analytics.getAddictionalProperty("report location", "");

        ReportAnalyzer reports = OutputParser.parse(location);

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

        DifferenceResults results = reports.findDifferences();

        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File((String)analytics.getAddictionalProperty("output file", "./report-analytics.json"));

            mapper.writeValue(file, results);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
