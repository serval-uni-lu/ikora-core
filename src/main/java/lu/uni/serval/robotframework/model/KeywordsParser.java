package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.Configuration;
import lu.uni.serval.utils.exception.DuplicateNodeException;
import lu.uni.serval.utils.tree.LabelTreeNode;

import java.util.Set;

public class KeywordsParser {

    public static Set<LabelTreeNode> parse(String location) throws DuplicateNodeException {
        Project project = ProjectFactory.load(location);

        KeywordTreeFactory keywordTreeFactory = new KeywordTreeFactory(project);
        Set<LabelTreeNode> forest = keywordTreeFactory.create();

        if(Configuration.getInstance().isVerbose()){
            System.out.println("----------------------------------------");
            System.out.println("KEYWORD LIST");
            System.out.println("----------------------------------------");
            for(LabelTreeNode tree: forest){
                System.out.println(tree.toString());
            }
        }

        return forest;
    }
}
