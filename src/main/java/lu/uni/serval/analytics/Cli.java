package lu.uni.serval.analytics;

import lu.uni.serval.utils.CommandRunner;
import lu.uni.serval.utils.tree.TreeNode;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Options;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Cli implements CommandRunner{
    private List<TreeNode> forest;

    public Cli(){
        this.forest = new ArrayList<>();
    }

    @Override
    public void run(CommandLine cmd) throws MissingArgumentException {
        if(!cmd.hasOption("output")){
            throw new MissingArgumentException("output");
        }

        CloneDetection cloneDetection = new CloneDetection();
        final CloneResults cloneResults = cloneDetection.findClones(forest);

        Statistics statistics = new Statistics();
        final StatisticsResults statisticsResults = statistics.computeStatistics(forest);

        try {
            XmlExport.write(statisticsResults, cloneResults, cmd.getOptionValue("output"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setCmdOptions(Options options) {
        options.addOption("output", true, "path to output file. type depends on the action");
        options.addOption("threshold", true, "threshold value under which two keywords are considered dissimilar");
    }

    public void setForest(final List<TreeNode> forest){
        this.forest = forest;
    }
}
