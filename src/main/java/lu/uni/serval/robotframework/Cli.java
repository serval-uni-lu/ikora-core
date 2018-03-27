package lu.uni.serval.robotframework;

import lu.uni.serval.robotframework.model.KeywordTreeFactory;
import lu.uni.serval.robotframework.model.TestCaseFile;
import lu.uni.serval.robotframework.model.TestCaseFileFactory;
import lu.uni.serval.utils.CommandRunner;
import lu.uni.serval.utils.tree.TreeNode;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Options;

import java.util.ArrayList;
import java.util.List;

public class Cli implements CommandRunner{
    private List<TreeNode> forest;

    public Cli(){
        this.forest = new ArrayList<>();
    }

    @Override
    public void run(CommandLine cmd) throws MissingArgumentException {
        if (!cmd.hasOption("file")) {
            throw new MissingArgumentException("file");
        }

        TestCaseFileFactory factory = new TestCaseFileFactory();
        TestCaseFile testCaseFile = factory.create(cmd.getOptionValue("file"));

        KeywordTreeFactory keywordTreeFactory = new KeywordTreeFactory(testCaseFile);
        forest = keywordTreeFactory.create();

        if(cmd.hasOption("verbose")){
            System.out.println("----------------------------------------");
            System.out.println("KEYWORD LIST");
            System.out.println("----------------------------------------");
            for(TreeNode tree: forest){
                System.out.println(tree.toString());
            }
        }
    }

    @Override
    public void setCmdOptions(Options options) {
        options.addOption("file", true, "path to RobotFramework testcase file");
    }

    public List<TreeNode> getForest() {
        return forest;
    }
}
