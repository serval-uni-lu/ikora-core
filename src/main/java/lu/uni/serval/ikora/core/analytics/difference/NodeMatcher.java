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

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.utils.LevenshteinDistance;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class NodeMatcher {
    enum Change {
        CHANGE_NAME, CHANGE_FOLDER, CHANGE_FILE, CHANGE_ALL
    }

    public static VersionPairs computeVersionsPairs(Projects version1, Projects version2, boolean ignoreProjectName){
        final List<Pair<TestCase, TestCase>> testCases = getPairs(version1.getTestCases(), version2.getTestCases(), ignoreProjectName);
        final List<Pair<UserKeyword, UserKeyword>> keywords = getPairs(version1.getUserKeywords(), version2.getUserKeywords(), ignoreProjectName);
        final List<Pair<VariableAssignment, VariableAssignment>> variables = getPairs(version1.getVariableAssignments(), version2.getVariableAssignments(), ignoreProjectName);

        int size = testCases.size() + keywords.size() + variables.size();
        final List<Pair<SourceNode, SourceNode>> pairs = new ArrayList<>(size);
        pairs.addAll(testCases.stream().map(p -> Pair.of((SourceNode)p.getLeft(), (SourceNode)p.getRight())).collect(Collectors.toList()));
        pairs.addAll(keywords.stream().map(p -> Pair.of((SourceNode)p.getLeft(), (SourceNode)p.getRight())).collect(Collectors.toList()));
        pairs.addAll(variables.stream().map(p -> Pair.of((SourceNode)p.getLeft(), (SourceNode)p.getRight())).collect(Collectors.toList()));

        final Set<Edit> edits = pairs.stream()
                .flatMap(p -> Difference.of(p.getLeft(), p.getRight()).getEdits().stream())
                .collect(Collectors.toSet());

        return new VersionPairs(resolvePairs(pairs, ignoreProjectName), edits, version1, version2);
    }

    public static <T extends SourceNode> List<Pair<T,T>> getPairs(Collection<T> nodes1, Collection<T> nodes2, boolean ignoreProjectName) {
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

    private static <T extends SourceNode> Set<T> matchNode(Collection<T> nodeList, T node, boolean ignoreProjectName){
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

        if(Difference.of(node1, node2).isEmpty()){
            return true;
        }

        if(node1.isHidden() || node2.isHidden()){
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

    private static <T extends SourceNode> Map<Change, List<T>> findPotentialCandidates(T t, List<T> unmatched) {
        String fileName = new File(t.getLibraryName()).getName();
        EnumMap<Change, List<T>> candidates = new EnumMap<>(Change.class);

        for (T current: unmatched){
            if(!t.differences(current).isEmpty()){
                continue;
            }

            String currentFileName = current.getLibraryName();

            if(current.getLibraryName().equals(t.getLibraryName())){
                List<T> list = candidates.getOrDefault(Change.CHANGE_NAME, new ArrayList<>());
                list.add(current);
                candidates.put(Change.CHANGE_NAME, list);
            }
            else if(current.getName().equals(t.getName()) && currentFileName.equals(fileName)){
                List<T> list = candidates.getOrDefault(Change.CHANGE_FOLDER, new ArrayList<>());
                list.add(current);
                candidates.put(Change.CHANGE_FOLDER, list);
            }
            else if(current.getName().equals(t.getName())){
                List<T> list = candidates.getOrDefault(Change.CHANGE_FILE, new ArrayList<>());
                list.add(current);
                candidates.put(Change.CHANGE_FILE, list);
            }
            else{
                List<T> list = candidates.getOrDefault(Change.CHANGE_ALL, new ArrayList<>());
                list.add(current);
                candidates.put(Change.CHANGE_ALL, list);
            }
        }

        return candidates;
    }

    private static <T extends SourceNode> T findBestCandidate(T t, List<T> unmatched){
        Map<Change, List<T>> candidates = findPotentialCandidates(t, unmatched);

        T bestCandidate = null;

        if(!candidates.getOrDefault(Change.CHANGE_NAME, new ArrayList<>()).isEmpty()){
            bestCandidate = candidates.get(Change.CHANGE_NAME).get(0);
        }
        else if(!candidates.getOrDefault(Change.CHANGE_FOLDER, new ArrayList<>()).isEmpty()){
            bestCandidate = candidates.get(Change.CHANGE_FOLDER).get(0);
        }
        else if(!candidates.getOrDefault(Change.CHANGE_FILE, new ArrayList<>()).isEmpty()){
            bestCandidate = candidates.get(Change.CHANGE_FILE).get(0);
        }
        else if(!candidates.getOrDefault(Change.CHANGE_ALL, new ArrayList<>()).isEmpty()){
            bestCandidate = candidates.get(Change.CHANGE_ALL).get(0);
        }

        if(bestCandidate != null){
            unmatched.remove(bestCandidate);
        }

        return bestCandidate;
    }

    private static List<Pair<SourceNode, SourceNode>> resolvePairs(List<Pair<SourceNode, SourceNode>> pairs, boolean ignoreProjectName){
        final List<Pair<SourceNode, SourceNode>> resolved = new ArrayList<>(pairs);

        for(Pair<SourceNode, SourceNode> pair: pairs){
            resolved.addAll(matchChildren(pair.getLeft(), pair.getRight(), ignoreProjectName));
        }

        return resolved;
    }

    private static List<Pair<SourceNode, SourceNode>> matchChildren(SourceNode node1, SourceNode node2, boolean ignoreProjectName){
        if(node1 == null || node2 == null){
            return Collections.emptyList();
        }

        List<Pair<SourceNode, SourceNode>> pairs;
        if(node1 instanceof NodeList && node2 instanceof NodeList){
            pairs = LevenshteinDistance.getMapping((NodeList<SourceNode>)node1, (NodeList<SourceNode>)node2);
        }
        else{
            pairs = LevenshteinDistance.getMapping(node1.getAstChildren(), node2.getAstChildren());
        }

        return resolvePairs(pairs, ignoreProjectName);
    }
}
