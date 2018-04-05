package lu.uni.serval.analytics;

import lu.uni.serval.utils.CommandRunner;
import lu.uni.serval.utils.Configuration;
import lu.uni.serval.utils.Plugin;
import lu.uni.serval.utils.exception.DuplicateNodeException;
import lu.uni.serval.utils.tree.TreeNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Cli implements CommandRunner{
    private List<TreeNode> forest;

    public Cli(){
        this.forest = new ArrayList<>();
    }

    @Override
    public void run() throws DuplicateNodeException {
        Configuration config = Configuration.getInstance();
        Plugin analytics = config.getPlugin("analytics");

        CloneIndex.setKeywordThreshold((double)analytics.getAddictionalProperty("keyword threshold", 0.9));
        CloneIndex.setTreeThreshold((double)analytics.getAddictionalProperty("tree threshold", 0.9));

        CloneDetection cloneDetection = new CloneDetection();
        final CloneResults cloneResults = cloneDetection.findClones(forest);

        Statistics statistics = new Statistics();
        final StatisticsResults statisticsResults = statistics.computeStatistics(forest);

        try {
            XmlExport.write(statisticsResults, cloneResults, (String)analytics.getAddictionalProperty("output file"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setForest(final List<TreeNode> forest){
        this.forest = forest;
    }
}
