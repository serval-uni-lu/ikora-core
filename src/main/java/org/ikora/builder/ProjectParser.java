package org.ikora.builder;

import org.ikora.Configuration;
import org.ikora.error.ErrorManager;
import org.apache.commons.io.FileUtils;
import org.ikora.model.Project;
import org.ikora.model.Resources;
import org.ikora.model.SourceFile;

import java.io.*;

import java.util.*;

class ProjectParser {
    private ProjectParser(){}

    public static Project parse(File file, Configuration configuration, DynamicImports dynamicImports, ErrorManager errors) {
        Project project = new Project(file);

        if(file.isDirectory()){
            file = addRobotFiles(file, project, configuration);
        }

        parseFiles(file, project, dynamicImports, errors);
        resolveResources(project, errors);

        return project;
    }

    private static File addRobotFiles(File directory, Project project, Configuration configuration) {
        String[] extensions = configuration.getExtensions().toArray(new String[0]);
        List<File> robots = (List<File>) FileUtils.listFiles(directory, extensions, true);

        List<File> ignoreList = getIgnoreList(project, configuration.getIgnorePath());

        for(File robot: robots){
            if(!isIgnored(robot, ignoreList)){
                project.addFile(robot);
            }
        }

        return getNextNotParsedFile(project);
    }

    private static void parseFiles(File file, Project project, DynamicImports dynamicImports, ErrorManager errors){
        if(file == null){
            return;
        }

        SourceFileParser.parse(file, project, dynamicImports, errors);

        parseFiles(getNextNotParsedFile(project), project, dynamicImports, errors);
    }

    private static void resolveResources(Project project, ErrorManager errors) {
        for(SourceFile sourceFile : project.getSourceFiles()) {
            for (Resources resources: sourceFile.getSettings().getResources()) {
                String name = project.generateFileName(resources.getFile());
                Optional<SourceFile> resourceFile = project.getSourceFile(name);

                if(resourceFile.isPresent()) {
                    resources.setSourceFile(resourceFile.get());
                }
                else{
                    errors.registerIOError(new File(project.getRootFolder(), name),"File not found");
                }
            }
        }
    }

    private static File getNextNotParsedFile(Project project){
        for (Map.Entry<String, SourceFile> file: project.getFiles().entrySet()){
            if(file.getValue() == null){
                return new File(project.getRootFolder(), file.getKey());
            }
        }

        return null;
    }

    private static List<File> getIgnoreList(Project project, List<String> ignorePaths){
        List<File> ignoreList = new ArrayList<>();

        for(String path: ignorePaths){
            ignoreList.add(new File(project.getRootFolder(), path));
        }

        return ignoreList;
    }

    private static boolean isIgnored(File file, List<File> ignoreList) {
        try {
            for(File ignoreFolder: ignoreList){
                if(org.ikora.utils.FileUtils.isSubDirectory(ignoreFolder, file)){
                    return true;
                }
            }
        }
        catch (IOException e){
            return false;
        }

        return false;
    }
}
