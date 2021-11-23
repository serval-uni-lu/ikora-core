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

import lu.uni.serval.ikora.core.exception.InvalidArgumentException;
import lu.uni.serval.ikora.core.BuildConfiguration;
import lu.uni.serval.ikora.core.error.ErrorManager;
import org.apache.commons.io.FileUtils;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.Resources;
import lu.uni.serval.ikora.core.model.Source;
import lu.uni.serval.ikora.core.model.SourceFile;

import java.io.*;

import java.util.*;

class ProjectParser {
    private ProjectParser(){}

    public static Project parse(File file, BuildConfiguration configuration, DynamicImports dynamicImports, ErrorManager errors) throws InvalidArgumentException {
        Source source = new Source(file);

        Project project = new Project(source);

        if(file.isDirectory()){
            source = addRobotFiles(file, project, configuration);
        }

        parseFiles(source, project, dynamicImports, errors);
        resolveResources(project, errors);

        return project;
    }

    public static Project parse(String string, DynamicImports dynamicImports, ErrorManager errors){
        Source source = new Source(string);

        Project project = new Project(source);
        SourceFileParser.parse(source, project, dynamicImports, errors);

        return project;
    }

    private static Source addRobotFiles(File directory, Project project, BuildConfiguration configuration) throws InvalidArgumentException {
        String[] extensions = configuration.getExtensions().toArray(new String[0]);
        List<File> robots = (List<File>) FileUtils.listFiles(directory, extensions, true);

        List<File> ignoreList = getIgnoreList(project, configuration.getIgnorePath());

        for(File robot: robots){
            if(!isIgnored(robot, ignoreList)){
                project.addFile(new Source(robot));
            }
        }

        return getNextNotParsedFile(project);
    }

    private static void parseFiles(Source source, Project project, DynamicImports dynamicImports, ErrorManager errors) throws InvalidArgumentException {
        if(source == null){
            return;
        }

        SourceFileParser.parse(source, project, dynamicImports, errors);

        parseFiles(getNextNotParsedFile(project), project, dynamicImports, errors);
    }

    private static void resolveResources(Project project, ErrorManager errors) throws InvalidArgumentException {
        for(SourceFile sourceFile : project.getSourceFiles()) {
            for (Resources resources: sourceFile.getSettings().getInternalResources()) {
                String name = project.generateFileName(new Source(resources.getFile()));
                Optional<SourceFile> resourceFile = project.getSourceFile(name);

                if(resourceFile.isPresent()) {
                    resources.setSourceFile(resourceFile.get());
                }
                else{
                    errors.registerIOError(new Source(project.getRootFolder(), name),"File not found");
                }
            }
        }
    }

    private static Source getNextNotParsedFile(Project project) throws InvalidArgumentException {
        for (Map.Entry<String, SourceFile> file: project.getFiles().entrySet()){
            if(file.getValue() == null){
                return new Source(project.getRootFolder(), file.getKey());
            }
        }

        return null;
    }

    private static List<File> getIgnoreList(Project project, List<String> ignorePaths){
        List<File> ignoreList = new ArrayList<>();

        for(String path: ignorePaths){
            ignoreList.add(new File(project.getRootFolder().asFile(), path));
        }

        return ignoreList;
    }

    private static boolean isIgnored(File file, List<File> ignoreList) {
        for(File ignoreFolder: ignoreList){
            if(lu.uni.serval.ikora.core.utils.FileUtils.isSubDirectory(ignoreFolder, file)){
                return true;
            }
        }

        return false;
    }
}
