package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.KeywordTable;
import lu.uni.serval.robotframework.model.Project;
import lu.uni.serval.robotframework.model.UserKeyword;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeywordMatcher {
    enum Edit{
        ChangeName, ChangeFolder, ChangeFile, ChangeAll
    }

    public static List<KeywordInfoPair> getPairs(Project project1, Project project2) {
        List<KeywordInfoPair> pairs = new ArrayList<>();

        KeywordTable keywords1 = project1.getUserKeywords();
        KeywordTable keywords2 = project2.getUserKeywords();

        List<UserKeyword> unmatched = new ArrayList<>();

        while(keywords1.size() > 0){
            UserKeyword keyword1 = keywords1.iterator().next();
            UserKeyword keyword2 = keywords2.findKeyword(keyword1);

            if(keyword2 == null){
                unmatched.add(keyword1);
            }
            else{
                pairs.add(KeywordInfoPair.of(project1, project2, keyword1, keyword2));
                keywords2.remove(keyword2);
            }

            keywords1.remove(keyword1);
        }

        while(keywords2.size() > 0){
            UserKeyword keyword2 = keywords2.iterator().next();
            UserKeyword keyword1 = findBestCandidate(keyword2, unmatched);

            pairs.add(KeywordInfoPair.of(project1, project2, keyword1, keyword2));
            keywords2.remove(keyword2);
        }

        while(unmatched.size() > 0){
            UserKeyword keyword1 = unmatched.iterator().next();

            pairs.add(KeywordInfoPair.of(project1, project2, keyword1, null));
            unmatched.remove(keyword1);
        }

        return pairs;
    }

    private static Map<Edit, List<UserKeyword>> findPotentialCandidates(UserKeyword keyword2, List<UserKeyword> unmatched) {
        String fileName = new File(keyword2.getFile()).getName();
        Map<Edit, List<UserKeyword>> candidates = new HashMap<>();

        for (UserKeyword current: unmatched){
            Action.Type[] ignore = {Action.Type.CHANGE_NAME};
            if(!Difference.of(keyword2, current).isEmpty(ignore)){
                continue;
            }

            String currentFileName = new File(current.getFile()).getName();

            if(current.getFile().equals(keyword2.getFile())){
                List<UserKeyword> list = candidates.getOrDefault(Edit.ChangeName, new ArrayList<>());
                list.add(current);
                candidates.put(Edit.ChangeName, list);
            }
            else if(current.getName().toString().equals(keyword2.getName().toString()) && currentFileName.equals(fileName)){
                List<UserKeyword> list = candidates.getOrDefault(Edit.ChangeFolder, new ArrayList<>());
                list.add(current);
                candidates.put(Edit.ChangeFolder, list);
            }
            else if(current.getName().toString().equals(keyword2.getName().toString())){
                List<UserKeyword> list = candidates.getOrDefault(Edit.ChangeFile, new ArrayList<>());
                list.add(current);
                candidates.put(Edit.ChangeFile, list);
            }
            else{
                List<UserKeyword> list = candidates.getOrDefault(Edit.ChangeAll, new ArrayList<>());
                list.add(current);
                candidates.put(Edit.ChangeAll, list);
            }
        }

        return candidates;
    }

    private static UserKeyword findBestCandidate(UserKeyword keyword, List<UserKeyword> unmatched){
        Map<Edit, List<UserKeyword>> candidates = findPotentialCandidates(keyword, unmatched);

        UserKeyword bestCandidate = null;

        if(!candidates.getOrDefault(Edit.ChangeName, new ArrayList<>()).isEmpty()){
            bestCandidate = candidates.get(Edit.ChangeName).get(0);
        }
        else if(!candidates.getOrDefault(Edit.ChangeFolder, new ArrayList<>()).isEmpty()){
            bestCandidate = candidates.get(Edit.ChangeFolder).get(0);
        }
        else if(!candidates.getOrDefault(Edit.ChangeFile, new ArrayList<>()).isEmpty()){
            bestCandidate = candidates.get(Edit.ChangeFile).get(0);
        }
        else if(!candidates.getOrDefault(Edit.ChangeAll, new ArrayList<>()).isEmpty()){
            bestCandidate = candidates.get(Edit.ChangeAll).get(0);
        }

        if(bestCandidate != null){
            unmatched.remove(bestCandidate);
        }

        return bestCandidate;
    }
}
