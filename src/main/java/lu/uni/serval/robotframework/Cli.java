package lu.uni.serval.robotframework;

import lu.uni.serval.robotframework.model.KeywordTreeFactory;
import lu.uni.serval.robotframework.model.Project;
import lu.uni.serval.robotframework.model.ProjectFactory;
import lu.uni.serval.utils.CommandRunner;
import lu.uni.serval.utils.Configuration;
import lu.uni.serval.utils.exception.DuplicateNodeException;
import lu.uni.serval.utils.tree.TreeNode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cli implements CommandRunner{
    private Set<TreeNode> forest;

    public Cli(){
        this.forest = new HashSet<>();
    }

    @Override
    public void run() throws DuplicateNodeException {
        Configuration config = Configuration.getInstance();
        Project project = ProjectFactory.load(config.getTestCaseFile());

        KeywordTreeFactory keywordTreeFactory = new KeywordTreeFactory(project);
        forest = keywordTreeFactory.create();

        if(config.isVerbose()){
            System.out.println("----------------------------------------");
            System.out.println("KEYWORD LIST");
            System.out.println("----------------------------------------");
            for(TreeNode tree: forest){
                System.out.println(tree.toString());
            }
        }
    }

    public Set<TreeNode> getForest() {
        return forest;
    }
}
