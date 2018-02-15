package lu.uni.serval;

import lu.uni.serval.robotframework.model.KeywordTreeFactory;
import lu.uni.serval.robotframework.model.TestCaseFile;
import lu.uni.serval.robotframework.model.TestCaseFileFactory;
import lu.uni.serval.robotframework.execution.Runner;
import lu.uni.serval.robotframework.report.Report;
import lu.uni.serval.utils.TreeNode;

import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.List;

public class RFTestGenerator {
    public static void main(String[] args) {
        try {
            Options options = new Options();
            options.addOption("file", true, "path to RobotFramework testcase file");

            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            if (!cmd.hasOption("file")) {
                throw new MissingArgumentException("file");
            }

            TestCaseFileFactory factory = new TestCaseFileFactory();
            TestCaseFile testCaseFile = factory.create(cmd.getOptionValue("file"));

            KeywordTreeFactory keywordTreeFactory = new KeywordTreeFactory(testCaseFile);
            List<TreeNode> forest = keywordTreeFactory.create();

            System.out.println(forest.toString());
            System.out.println("----------------------------------------");

            for(TreeNode root : forest){
                System.out.println(root.getLabel());
                for(TreeNode leaf : root.getLeaves()) {
                    System.out.println("\t" + leaf.getLabel());
                }
            }

            Runner runner = new Runner();

            List<Report> reports = new ArrayList<Report>();

            for(TreeNode root : forest) {
                Report report = runner.executeKeyword(root);
                report.setName(root.data.toString());
                reports.add(report);
            }

            for(Report report: reports) {
                report.print();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
