package org.ukwikora.analytics;

import org.apache.commons.lang3.tuple.Pair;
import org.ukwikora.model.*;

import java.io.File;
import java.util.*;

public class StatementMatcher {
    enum Edit{
        ChangeName, ChangeFolder, ChangeFile, ChangeAll
    }

    public static <T extends Statement> List<Pair<T,T>> getPairs(Class<T> type, Project project1, Project project2) {
        List<Pair<T,T>> pairs = new ArrayList<>();

        StatementTable<T> statement1 = createStatementTable(project1, type);
        StatementTable<T> statement2 = createStatementTable(project2, type);

        List<T> unmatched = new ArrayList<>();

        while(statement1.size() > 0){
            T keyword1 = statement1.iterator().next();
            T keyword2 = statement2.findStatement(keyword1);

            if(keyword2 == null){
                unmatched.add(keyword1);
            }
            else{
                pairs.add(Pair.of(keyword1, keyword2));
                statement2.remove(keyword2);
            }

            statement1.remove(keyword1);
        }

        while(statement2.size() > 0){
            T keyword2 = statement2.iterator().next();
            T keyword1 = findBestCandidate(keyword2, unmatched);

            pairs.add(Pair.of(keyword1, keyword2));
            statement2.remove(keyword2);
        }

        while(unmatched.size() > 0){
            T keyword1 = unmatched.iterator().next();

            pairs.add(Pair.of(keyword1, null));
            unmatched.remove(keyword1);
        }

        return pairs;
    }

    private static <T extends Statement> StatementTable<T> createStatementTable(Project project, Class<T> type) {
        StatementTable<T> table = new StatementTable<>();

        for (Statement statements: project.getStatements(type)) {
            table.add((T)statements);
        }

        return table;
    }

    private static <T extends Statement> Map<Edit, List<T>> findPotentialCandidates(T keyword, List<T> unmatched) {
        String fileName = new File(keyword.getFileName()).getName();
        Map<Edit, List<T>> candidates = new HashMap<>();

        for (T current: unmatched){
            if(!CloneDetection.isCloneTypeI(keyword.getClass(), Difference.of(keyword, current))){
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

    private static <T extends Statement> T findBestCandidate(T keyword, List<T> unmatched){
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
