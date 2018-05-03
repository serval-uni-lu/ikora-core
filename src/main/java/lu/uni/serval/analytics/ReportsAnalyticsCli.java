package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.report.OutputParser;
import lu.uni.serval.robotframework.report.Report;
import lu.uni.serval.utils.CommandRunner;
import lu.uni.serval.utils.Configuration;
import lu.uni.serval.utils.Plugin;
import lu.uni.serval.utils.exception.DuplicateNodeException;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ReportsAnalyticsCli implements CommandRunner {
    @Override
    public void run() throws DuplicateNodeException {
        Configuration config = Configuration.getInstance();
        Plugin analytics = config.getPlugin("report analytics");
        String location = (String)analytics.getAddictionalProperty("report location", "");

        ReportAnalyzer reports = OutputParser.parse(location);
        try {

            for(Report report: reports){
                String fileName = "C:\\Users\\renaud.rwemalika\\Desktop\\report_" + report.getCreationTime().toLocalDate() + ".txt";

                PrintWriter out = new PrintWriter(fileName);
                out.print(report.getKeywords());
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        reports.findDifferences();
    }
}
