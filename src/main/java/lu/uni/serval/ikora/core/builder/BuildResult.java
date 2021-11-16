package lu.uni.serval.ikora.core.builder;

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

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.Projects;
import lu.uni.serval.ikora.core.model.SourceFile;

import java.net.URI;
import java.util.*;

public class BuildResult {
    private ErrorManager errors;
    private Projects projects;

    private long parsingTime;
    private long dependencyResolutionTime;
    private long resolveTime;
    private long buildTime;

    public BuildResult(){
        this.errors = new ErrorManager();
        this.projects = new Projects();

        parsingTime = -1;
        dependencyResolutionTime = -1;
        resolveTime = -1;
        buildTime = -1;
    }

    public ErrorManager getErrors() {
        return errors;
    }

    public Projects getProjects() {
        return projects;
    }

    public Optional<Project> getProject(String name){
        return projects.findProjectByName(name);
    }

    public void setErrors(ErrorManager errors) {
        this.errors = errors;
    }

    public long getParsingTime() {
        return parsingTime;
    }

    public void setParsingTime(long parsingTime) {
        this.parsingTime = parsingTime;
    }

    public long getDependencyResolutionTime() {
        return dependencyResolutionTime;
    }

    public void setDependencyResolutionTime(long dependencyResolutionTime) {
        this.dependencyResolutionTime = dependencyResolutionTime;
    }

    public long getResolveTime() {
        return resolveTime;
    }

    public void setResolveTime(long resolveTime) {
        this.resolveTime = resolveTime;
    }

    public long getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(long buildTime) {
        this.buildTime = buildTime;
    }

    public void setProjects(Projects projects) {
        this.projects = projects;
    }

    public SourceFile getSourceFile(URI uri){
        for(Project project: projects){
            Optional<SourceFile> optional = project.getSourceFile(uri);

            if(optional.isPresent()){
                return optional.get();
            }
        }

        return null;
    }
}
