package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.KeywordDefinition;
import lu.uni.serval.robotframework.model.KeywordTable;
import lu.uni.serval.robotframework.model.Project;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeywordMatcher<T> {
    enum Edit{
        ChangeName, ChangeFolder, ChangeFile, ChangeAll
    }

    public static <T extends KeywordDefinition> List<ElementInfoPair<T>> getPairs(Class<T> type, Project project1, Project project2) {
        List<ElementInfoPair<T>> pairs = new ArrayList<>();

        KeywordTable<T> keywords1 = project1.getKeywords(type);
        KeywordTable<T> keywords2 = project2.getKeywords(type);

        List<T> unmatched = new ArrayList<>();

        while(keywords1.size() > 0){
            T keyword1 = keywords1.iterator().next();
            T keyword2 = keywords2.findKeyword(keyword1);

            if(keyword2 == null){
                unmatched.add(keyword1);
            }
            else{
                pairs.add(ElementInfoPair.of(project1, project2, keyword1, keyword2));
                keywords2.remove(keyword2);
            }

            keywords1.remove(keyword1);
        }

        while(keywords2.size() > 0){
            T keyword2 = keywords2.iterator().next();
            T keyword1 = findBestCandidate(type, keyword2, unmatched);

            pairs.add(ElementInfoPair.of(project1, project2, keyword1, keyword2));
            keywords2.remove(keyword2);
        }

        while(unmatched.size() > 0){
            T keyword1 = unmatched.iterator().next();

            pairs.add(ElementInfoPair.of(project1, project2, keyword1, null));
            unmatched.remove(keyword1);
        }

        return pairs;
    }

    private static <T extends KeywordDefinition> Map<Edit, List<T>> findPotentialCandidates(Class<T> type, T keyword2, List<T> unmatched) {
        String fileName = new File(keyword2.getFile()).getName();
        Map<Edit, List<T>> candidates = new HashMap<>();

        for (T current: unmatched){
            Action.Type[] ignore = {Action.Type.CHANGE_NAME};
            if(!Difference.of(keyword2, current).isEmpty(ignore)){
                continue;
            }

            String currentFileName = new File(current.getFile()).getName();

            if(current.getFile().equals(keyword2.getFile())){
                List<T> list = candidates.getOrDefault(Edit.ChangeName, new ArrayList<>());
                list.add(current);
                candidates.put(Edit.ChangeName, list);
            }
            else if(current.getName().toString().equals(keyword2.getName().toString()) && currentFileName.equals(fileName)){
                List<T> list = candidates.getOrDefault(Edit.ChangeFolder, new ArrayList<>());
                list.add(current);
                candidates.put(Edit.ChangeFolder, list);
            }
            else if(current.getName().toString().equals(keyword2.getName().toString())){
                List<T> list = candidates.getOrDefault(Edit.ChangeFile, new ArrayList<>());
                list.add(current);
                candidates.put(Edit.ChangeFile, list);
            }
            else{
                List<T> list = candidates.getOrDefault(Edit.ChangeAll, new ArrayList<>());
                list.add(current);
                candidates.put(Edit.ChangeAll, list);
            }
        }

        return candidates;
    }

    private static <T extends KeywordDefinition> T findBestCandidate(Class<T> type, T keyword, List<T> unmatched){
        Map<Edit, List<T>> candidates = findPotentialCandidates(type, keyword, unmatched);

        T bestCandidate = null;

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
