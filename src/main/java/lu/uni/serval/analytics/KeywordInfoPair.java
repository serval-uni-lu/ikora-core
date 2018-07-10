package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.KeywordDefinition;
import lu.uni.serval.robotframework.model.Project;

public class KeywordInfoPair {
    private KeywordInfo left;
    private KeywordInfo right;

    static public KeywordInfoPair of(Project project1, Project project2, KeywordDefinition keyword1, KeywordDefinition keyword2){
        KeywordInfoPair pair = new KeywordInfoPair();

        pair.left = new KeywordInfo(project1, keyword1);
        pair.right = new KeywordInfo(project2, keyword2);

        return pair;
    }


    public KeywordInfo getLeft() {
        return left;
    }

    public KeywordInfo getRight() {
        return right;
    }

    public KeywordDefinition getKeyword(Project project){
        if(project == left.getProject()){
            return left.getKeyword();
        }
        else if(project == right.getProject()){
            return right.getKeyword();
        }

        return null;
    }
}
