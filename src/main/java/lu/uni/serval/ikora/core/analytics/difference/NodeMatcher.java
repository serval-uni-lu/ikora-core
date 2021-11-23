package lu.uni.serval.ikora.core.analytics.difference;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.SourceNode;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.*;

public class NodeMatcher {
    enum Edit{
        CHANGE_NAME, CHANGE_FOLDER, CHANGE_FILE, CHANGE_ALL
    }

    public static <T extends SourceNode> List<Pair<T,T>> getPairs(Set<T> nodes1, Set<T> nodes2, boolean ignoreProjectName) {
        List<Pair<T,T>> pairs = new ArrayList<>();
        List<T> unmatched = new ArrayList<>();

        while(!nodes1.isEmpty()){
            T node1 = nodes1.iterator().next();
            Set<T> nodesFound2 = matchNode(nodes2, node1, ignoreProjectName);

            if(nodesFound2.isEmpty()){
                unmatched.add(node1);
            }
            else{
                //TODO: Find best match if multiple hits
                T found = nodesFound2.iterator().next();

                pairs.add(Pair.of(node1, found));
                nodes2.remove(found);
            }

            nodes1.remove(node1);
        }

        while(!nodes2.isEmpty()){
            T t2 = nodes2.iterator().next();
            T t1 = findBestCandidate(t2, unmatched);

            pairs.add(Pair.of(t1, t2));
            nodes2.remove(t2);
        }

        while(!unmatched.isEmpty()){
            T t1 = unmatched.iterator().next();

            pairs.add(Pair.of(t1, null));
            unmatched.remove(t1);
        }

        return pairs;
    }

    private static <T extends SourceNode> Set<T> matchNode(Set<T> nodeList, T node, boolean ignoreProjectName){
        Set<T> nodesFound = new HashSet<>();

        for(T currentNode: nodeList){
            if(matches(node, currentNode, ignoreProjectName)){
                nodesFound.add(currentNode);
            }
        }

        return nodesFound;
    }

    private static boolean matches(SourceNode node1, SourceNode node2, boolean ignoreProjectName){
        if(!ignoreProjectName && !isSameProject(node1, node2)){
            return false;
        }

        if(!isSameFile(node1, node2)){
            return false;
        }

        return node1.matches(node2.getDefinitionToken());
    }

    private static boolean isSameProject(SourceNode node1, SourceNode node2){
        Project project1 = node1.getProject();
        Project project2 = node2.getProject();

        if(project1 == project2){
            return true;
        }

        if(project1 == null || project2 == null){
            return false;
        }

        return project1.getName().equalsIgnoreCase(project2.getName());
    }

    private static boolean isSameFile(SourceNode node1, SourceNode node2){
        return node1.getLibraryName().equalsIgnoreCase(node2.getLibraryName());
    }

    private static <T extends SourceNode> Map<Edit, List<T>> findPotentialCandidates(T t, List<T> unmatched) {
        String fileName = new File(t.getLibraryName()).getName();
        EnumMap<Edit, List<T>> candidates = new EnumMap<>(Edit.class);

        for (T current: unmatched){
            if(t.distance(current) != 0.){
                continue;
            }

            String currentFileName = current.getLibraryName();

            if(current.getLibraryName().equals(t.getLibraryName())){
                List<T> list = candidates.getOrDefault(Edit.CHANGE_NAME, new ArrayList<>());
                list.add(current);
                candidates.put(Edit.CHANGE_NAME, list);
            }
            else if(current.getName().equals(t.getName()) && currentFileName.equals(fileName)){
                List<T> list = candidates.getOrDefault(Edit.CHANGE_FOLDER, new ArrayList<>());
                list.add(current);
                candidates.put(Edit.CHANGE_FOLDER, list);
            }
            else if(current.getName().equals(t.getName())){
                List<T> list = candidates.getOrDefault(Edit.CHANGE_FILE, new ArrayList<>());
                list.add(current);
                candidates.put(Edit.CHANGE_FILE, list);
            }
            else{
                List<T> list = candidates.getOrDefault(Edit.CHANGE_ALL, new ArrayList<>());
                list.add(current);
                candidates.put(Edit.CHANGE_ALL, list);
            }
        }

        return candidates;
    }

    private static <T extends SourceNode> T findBestCandidate(T t, List<T> unmatched){
        Map<Edit, List<T>> candidates = findPotentialCandidates(t, unmatched);

        T bestCandidate = null;

        if(!candidates.getOrDefault(Edit.CHANGE_NAME, new ArrayList<>()).isEmpty()){
            bestCandidate = candidates.get(Edit.CHANGE_NAME).get(0);
        }
        else if(!candidates.getOrDefault(Edit.CHANGE_FOLDER, new ArrayList<>()).isEmpty()){
            bestCandidate = candidates.get(Edit.CHANGE_FOLDER).get(0);
        }
        else if(!candidates.getOrDefault(Edit.CHANGE_FILE, new ArrayList<>()).isEmpty()){
            bestCandidate = candidates.get(Edit.CHANGE_FILE).get(0);
        }
        else if(!candidates.getOrDefault(Edit.CHANGE_ALL, new ArrayList<>()).isEmpty()){
            bestCandidate = candidates.get(Edit.CHANGE_ALL).get(0);
        }

        if(bestCandidate != null){
            unmatched.remove(bestCandidate);
        }

        return bestCandidate;
    }
}
