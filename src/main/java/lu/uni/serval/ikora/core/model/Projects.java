package lu.uni.serval.ikora.core.model;

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

import java.time.Instant;
import java.util.*;

public class Projects implements Iterable<Project> {
    private Instant date;
    private String versionId;
    private Set<Project> projectSet;

    public Projects(){
        this.projectSet = new HashSet<>();
    }

    public Projects(Project project){
        this.projectSet = new HashSet<>();
        this.projectSet.add(project);
    }

    public Projects(Set<Project> projects){
        this.projectSet = projects;
    }

    public Set<Project> asSet() {
        return this.projectSet;
    }

    public void addProject(Project project){
        if(project == null){
            return;
        }

        projectSet.add(project);
    }

    public void addProjects(Projects projects){
        for (Project project: projects.projectSet){
            addProject(project);
        }
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getVersionId() {
        if(versionId == null && date != null){
            return date.toString();
        }

        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public boolean contains(Project project){
        return projectSet.contains(project);
    }

    public Optional<Project> findProjectByName(String name){
        for(Project project: projectSet){
            if(project.getName().equalsIgnoreCase(name)){
                return Optional.of(project);
            }
        }

        return Optional.empty();
    }

    public int getLoc() {
        return projectSet.stream().reduce(0, (total, project) -> total + project.getLoc(), Integer::sum);
    }

    public Set<TestCase> getTestCases(){
        return projectSet.stream().collect(HashSet::new, (total, project) -> total.addAll(project.getTestCases()), HashSet::addAll);
    }

    public Set<UserKeyword> getUserKeywords(){
        return projectSet.stream().collect(HashSet::new, (total, project) -> total.addAll(project.getUserKeywords()), HashSet::addAll);
    }

    public Set<VariableAssignment> getVariableAssignments(){
        return projectSet.stream().collect(HashSet::new, (total, project) -> total.addAll(project.getVariableAssignments()), HashSet::addAll);
    }

    @Override
    public Iterator<Project> iterator() {
        return projectSet.iterator();
    }

    public int size() {
        return projectSet.size();
    }

    public boolean isEmpty() {
        return projectSet.isEmpty();
    }
}
