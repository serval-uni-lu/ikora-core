package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.Element;
import lu.uni.serval.robotframework.model.ElementTable;
import lu.uni.serval.robotframework.model.Project;

import java.io.File;
import java.util.*;

public class ElementMatcher {
    enum Edit{
        ChangeName, ChangeFolder, ChangeFile, ChangeAll
    }

    public static <T extends Element> List<ElementInfoPair<T>> getPairs(Class<T> type, Project project1, Project project2) {
        List<ElementInfoPair<T>> pairs = new ArrayList<>();

        ElementTable<T> elements1 = project1.getElements(type);
        ElementTable<T> elements2 = project2.getElements(type);

        List<T> unmatched = new ArrayList<>();

        while(elements1.size() > 0){
            T keyword1 = elements1.iterator().next();
            T keyword2 = elements2.findElement(keyword1);

            if(keyword2 == null){
                unmatched.add(keyword1);
            }
            else{
                pairs.add(ElementInfoPair.of(project1, project2, keyword1, keyword2));
                elements2.remove(keyword2);
            }

            elements1.remove(keyword1);
        }

        while(elements2.size() > 0){
            T keyword2 = elements2.iterator().next();
            T keyword1 = findBestCandidate(keyword2, unmatched);

            pairs.add(ElementInfoPair.of(project1, project2, keyword1, keyword2));
            elements2.remove(keyword2);
        }

        while(unmatched.size() > 0){
            T keyword1 = unmatched.iterator().next();

            pairs.add(ElementInfoPair.of(project1, project2, keyword1, null));
            unmatched.remove(keyword1);
        }

        return pairs;
    }

    private static <T extends Element> Map<Edit, List<T>> findPotentialCandidates(T keyword, List<T> unmatched) {
        String fileName = new File(keyword.getFileName()).getName();
        Map<Edit, List<T>> candidates = new HashMap<>();

        for (T current: unmatched){
            if(!Difference.of(keyword, current).isCloneTypeI()){
                continue;
            }

            String currentFileName = current.getFileName();

            if(current.getFile().equals(keyword.getFile())){
                List<T> list = candidates.getOrDefault(Edit.ChangeName, new ArrayList<>());
                list.add(current);
                candidates.put(Edit.ChangeName, list);
            }
            else if(current.getName().equals(keyword.getName()) && currentFileName.equals(fileName)){
                List<T> list = candidates.getOrDefault(Edit.ChangeFolder, new ArrayList<>());
                list.add(current);
                candidates.put(Edit.ChangeFolder, list);
            }
            else if(current.getName().equals(keyword.getName())){
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

    private static <T extends Element> T findBestCandidate(T keyword, List<T> unmatched){
        Map<Edit, List<T>> candidates = findPotentialCandidates(keyword, unmatched);

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
