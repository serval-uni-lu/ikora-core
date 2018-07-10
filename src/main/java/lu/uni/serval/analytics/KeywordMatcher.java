package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.KeywordTable;
import lu.uni.serval.robotframework.model.Project;
import lu.uni.serval.robotframework.model.UserKeyword;

import java.util.ArrayList;
import java.util.List;

public class KeywordMatcher {

    public static List<KeywordInfoPair> getPairs(Project project1, Project project2) {
        List<KeywordInfoPair> pairs = new ArrayList<>();

        KeywordTable keywords1 = project1.getUserKeywords();
        KeywordTable keywords2 = project2.getUserKeywords();

        while(keywords1.size() > 0){
            UserKeyword keyword1 = keywords1.iterator().next();
            UserKeyword keyword2 = keywords2.findKeyword(keyword1);

            pairs.add(KeywordInfoPair.of(project1, project2, keyword1, keyword2));

            keywords1.remove(keyword1);
            keywords2.remove(keyword2);
        }

        while(keywords2.size() > 0){
            UserKeyword keyword2 = keywords2.iterator().next();
            pairs.add(KeywordInfoPair.of(project1, project2, null, keyword2));
            keywords2.remove(keyword2);
        }

        return pairs;
    }
}
