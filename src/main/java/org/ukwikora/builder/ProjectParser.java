package org.ukwikora.builder;

import org.ukwikora.error.Error;
import org.ukwikora.error.IOError;
import org.ukwikora.utils.Configuration;
import org.ukwikora.utils.Plugin;
import org.apache.commons.io.FileUtils;
import org.ukwikora.model.Project;
import org.ukwikora.model.Resources;
import org.ukwikora.model.SourceFile;

import java.io.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class ProjectParser {
    private ProjectParser(){}

    public static Project parse(File file, DynamicImports dynamicImports, List<Error> errors) {
        Project project = new Project(file);

        if(file.isDirectory()){
            file = addRobotFiles(file, project);
        }

        parseFiles(file, project, dynamicImports, errors);
        resolveResources(project, errors);

        return project;
    }

    private static File addRobotFiles(File directory, Project project) {
        String[] extensions = new String[] { "robot" };
        List<File> robots = (List<File>) FileUtils.listFiles(directory, extensions, true);

        List<File> ignoreList = getIgnoreList(project);

        for(File robot: robots){
            if(!isIgnored(robot, ignoreList)){
                project.addFile(robot);
            }
        }

        return getNextNotParsedFile(project);
    }

    private static void parseFiles(File file, Project project, DynamicImports dynamicImports, List<Error> errors){
        if(file == null){
            return;
        }

        SourceFileParser.parse(file, project, dynamicImports, errors);

        parseFiles(getNextNotParsedFile(project), project, dynamicImports, errors);
    }

    private static void resolveResources(Project project, List<Error> errors) {
        for(SourceFile sourceFile : project.getSourceFiles()) {
            for (Resources resources: sourceFile.getSettings().getResources()) {
                String name = project.generateFileName(resources.getFile());
                SourceFile resourceFile = project.getSourceFile(name);

                if(resourceFile == null) {
                    IOError error = new IOError("File not found", new File(project.getRootFolder(), name));
                    errors.add(error);
                }
                else{
                    resources.setSourceFile(resourceFile);
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

    private static List<File> getIgnoreList(Project project){
        Configuration configuration = Configuration.getInstance();
        Plugin plugin = configuration.getPlugin("project analytics");

        if(plugin == null){
            return Collections.emptyList();
        }

        List<String> paths = (List<String>) plugin.getAdditionalProperty("exclude folders", new ArrayList<String>());
        List<File> ignoreList = new ArrayList<>();

        for(String path: paths){
            ignoreList.add(new File(project.getRootFolder(), path));
        }

        return ignoreList;
    }

    private static boolean isIgnored(File file, List<File> ignoreList) {
        try {
            for(File ignoreFolder: ignoreList){
                if(org.ukwikora.utils.FileUtils.isSubDirectory(ignoreFolder, file)){
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
