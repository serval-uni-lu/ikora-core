package lu.uni.serval;

import lu.uni.serval.analytics.CloneDetection;
import lu.uni.serval.analytics.CloneResults;
import lu.uni.serval.robotframework.model.KeywordTreeFactory;
import lu.uni.serval.robotframework.model.TestCaseFile;
import lu.uni.serval.robotframework.model.TestCaseFileFactory;
import lu.uni.serval.utils.tree.TreeNode;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.ParseException;

import java.util.List;
import java.util.Map;

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

            System.out.println("----------------------------------------");
            System.out.println("KEYWORD LIST");
            System.out.println("----------------------------------------");
            for(TreeNode tree: forest){
                System.out.println(tree.toString());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
