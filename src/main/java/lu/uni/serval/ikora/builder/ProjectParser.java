package lu.uni.serval.ikora.builder;

import lu.uni.serval.ikora.exception.InvalidArgumentException;
import lu.uni.serval.ikora.BuildConfiguration;
import lu.uni.serval.ikora.error.ErrorManager;
import org.apache.commons.io.FileUtils;
import lu.uni.serval.ikora.model.Project;
import lu.uni.serval.ikora.model.Resources;
import lu.uni.serval.ikora.model.Source;
import lu.uni.serval.ikora.model.SourceFile;

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
            if(lu.uni.serval.ikora.utils.FileUtils.isSubDirectory(ignoreFolder, file)){
                return true;
            }
        }

        return false;
    }
}
