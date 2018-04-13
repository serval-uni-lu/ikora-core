package lu.uni.serval.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import lu.uni.serval.utils.CommandRunner;
import lu.uni.serval.utils.Configuration;
import lu.uni.serval.utils.Plugin;
import lu.uni.serval.utils.exception.DuplicateNodeException;
import lu.uni.serval.utils.tree.TreeNode;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class Cli implements CommandRunner{
    private Set<TreeNode> forest;

    public Cli(){
        this.forest = null;
    }

    @Override
    public void run() throws DuplicateNodeException {
        if(this.forest == null){
            return;
        }

        Configuration config = Configuration.getInstance();
        Plugin analytics = config.getPlugin("analytics");

        CloneIndex.setKeywordThreshold((double)analytics.getAddictionalProperty("keyword threshold", 0.9));
        CloneIndex.setTreeThreshold((double)analytics.getAddictionalProperty("tree threshold", 0.9));

        CloneDetection cloneDetection = new CloneDetection();
        final CloneResults cloneResults = cloneDetection.findClones(forest);

        Statistics statistics = new Statistics();
        final StatisticsResults statisticsResults = statistics.computeStatistics(forest);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonExport export = new JsonExport();
            export.setClones(cloneResults);
            export.setGeneralStatistics(statisticsResults);

            File file = new File((String)analytics.getAddictionalProperty("output file", "./analytics.json"));

            mapper.writeValue(file, export);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setForest(final Set<TreeNode> forest){
        this.forest = forest;
    }
}
