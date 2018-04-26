package lu.uni.serval.robotframework.model;

import lu.uni.serval.robotframework.model.KeywordTreeFactory;
import lu.uni.serval.robotframework.model.Project;
import lu.uni.serval.robotframework.model.ProjectFactory;
import lu.uni.serval.utils.Configuration;
import lu.uni.serval.utils.exception.DuplicateNodeException;
import lu.uni.serval.utils.tree.TreeNode;

import java.util.Set;

public class KeywordsParser {

    public static Set<TreeNode> parse(String location) throws DuplicateNodeException {
        Project project = ProjectFactory.load(location);

        KeywordTreeFactory keywordTreeFactory = new KeywordTreeFactory(project);
        Set<TreeNode> forest = keywordTreeFactory.create();

        if(Configuration.getInstance().isVerbose()){
            System.out.println("----------------------------------------");
            System.out.println("KEYWORD LIST");
            System.out.println("----------------------------------------");
            for(TreeNode tree: forest){
                System.out.println(tree.toString());
            }
        }

        return forest;
    }
}
