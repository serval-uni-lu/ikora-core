package lu.uni.serval;

import lu.uni.serval.robotframework.model.KeywordTreeFactory;
import lu.uni.serval.robotframework.model.TestCaseFile;
import lu.uni.serval.robotframework.model.TestCaseFileFactory;
import lu.uni.serval.utils.KeywordData;
import lu.uni.serval.utils.TreeNode;

import org.apache.commons.cli.*;

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

            List<TreeNode<KeywordData>> forest = KeywordTreeFactory.create(testCaseFile);

            System.out.println(forest.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
