package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.exception.MissingAttributeException;
import org.apache.commons.io.FileUtils;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProjectFactory {
    private static PyObject testCaseFileClass;

    static{
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec( "from robot.api import TestCaseFile");

        testCaseFileClass = interpreter.get("TestCaseFile");
    }

    static public Project load(String path){
        Project project = new Project();

            try {
                File file = new File(path);

                if(file.isFile()){
                    createFile(path, project);
                }
                else if (file.isDirectory()){
                    createFiles(path, project);
                }
            } catch (MissingAttributeException e) {
                e.printStackTrace();
            }

        return project;
    }

    private static void createFiles(String path, Project project) throws MissingAttributeException {
        String extensions[] = {"robot"};
        Collection<File> robotFiles = FileUtils.listFiles(new File(path), extensions, true);

        for(File robotFile: robotFiles){
            String filePath = robotFile.getAbsolutePath();
            if(project.hasFile(filePath)){
                continue;
            }

            createFile(robotFile.getAbsolutePath(), project);
        }
    }

    static private void createFile(String filePath, Project project) throws MissingAttributeException {
        PyObject testCaseFileObject = testCaseFileClass.__call__(new PyString(""), new PyString(filePath));
        testCaseFileObject.__findattr__("populate").__call__();

        String directory = getStringValue(testCaseFileObject, "directory");
        String name = getStringValue(testCaseFileObject, "name");
        Settings settings = createSettingsTable(testCaseFileObject.__findattr__("setting_table"));
        TestCaseTable testCaseTable = createTestCaseTable(testCaseFileObject, filePath);
        KeywordTable keywordTable = createKeywordTable(testCaseFileObject.__findattr__("keyword_table"), filePath);
        VariableTable variableTable = createVariableTable(testCaseFileObject.__findattr__("variable_table"));

        loadResources(project, settings, directory);

        TestCaseFile file = new TestCaseFile(directory, filePath, name, settings, testCaseTable, keywordTable, variableTable);

        project.addTestCaseFile(file);
    }

    private static TestCaseTable createTestCaseTable(PyObject testCaseFileObject, String filePath) throws MissingAttributeException {
        TestCaseTable testCaseTable = new TestCaseTable();

        PyObject pyTestCaseTable = testCaseFileObject.__findattr__("testcase_table");

        for (PyObject pyTestCase : pyTestCaseTable.asIterable()){
            testCaseTable.add(createTestCase(pyTestCase, filePath));
        }

        return testCaseTable;
    }

    private static UserKeyword createTestCase(PyObject pyTestCase, String filePath) throws MissingAttributeException {
        String name = getStringValue(pyTestCase, "name");
        String documentation = getStringValue(pyTestCase, "doc");
        List<Step> steps = createSteps(pyTestCase.__findattr__("steps"), filePath);
        List<String> tags = getStringListValue(pyTestCase.__findattr__("tags"));

        return new UserKeyword(filePath, name, new ArrayList<>(), documentation, steps, tags);
    }

    protected static VariableTable createVariableTable(PyObject pyVariableTable) {

        VariableTable variableTable = new VariableTable();

        for (PyObject pyVariable : pyVariableTable.__findattr__("variables").asIterable()){
            String name = pyVariable.__findattr__("name").toString();
            List<String> value = getStringListValue(pyVariable.__findattr__("value"));
            variableTable.put(name, value);
        }

        return variableTable;
    }

    protected static Settings createSettingsTable(PyObject pySettings) throws MissingAttributeException {
        ResourcesTable resourcesTable = createImportTable(pySettings.__findattr__("imports"));

        return new Settings(resourcesTable);
    }

    protected static ResourcesTable createImportTable(PyObject pyImports) throws MissingAttributeException {
        ResourcesTable resourcesTable = new ResourcesTable();

        for (PyObject pyResource : pyImports.__findattr__("data").asIterable()) {
            String name = getStringValue(pyResource, "name");
            List<String> arguments = getStringListValue(pyResource.__findattr__("args"), "args");
            String comment = getStringValue(pyResource,"comment");

            Resources.Type type = getResourceType(pyResource);

            resourcesTable.add(new Resources(type, name, arguments, comment));
        }

        return resourcesTable;
    }

    private static Resources.Type getResourceType(PyObject pyResource) {
        Resources.Type type = Resources.Type.Unknown;
        String objectType = pyResource.getType().toString();

        if(objectType.equals("<class 'robot.parsing.settings.Library'>")){
            type = Resources.Type.Library;
        }
        else if (objectType.equals("<class 'robot.parsing.settings.Resource'>")) {
            type = Resources.Type.Resource;
        }

        return type;
    }

    protected static KeywordTable createKeywordTable(PyObject pyKeywordTable, String filePath){
        KeywordTable keywordTable = new KeywordTable();

        for (PyObject pyUserKeyword : pyKeywordTable.asIterable()){
            try {
                keywordTable.add(createUserKeyword(pyUserKeyword, filePath));
            } catch (MissingAttributeException e) {
                System.out.println(e.getMessage());
            }
        }

        return keywordTable;
    }

    private static UserKeyword createUserKeyword(PyObject pyUserKeyword, String filePath) throws MissingAttributeException {
        String name = getStringValue(pyUserKeyword, "name");
        String documentation = getStringValue(pyUserKeyword, "doc");
        List<String> arguments = createArguments(pyUserKeyword.__findattr__("args"));
        List<Step> steps = createSteps(pyUserKeyword.__findattr__("steps"), filePath);
        List<String> tags = getStringListValue(pyUserKeyword.__findattr__("tags"));

        return new UserKeyword(filePath, name, arguments, documentation, steps, tags);
    }

    private static List<String> createArguments(PyObject pyArguments) {
        List<String> arguments = new ArrayList<>();

        for (PyObject pyArgument : pyArguments.asIterable()){
            arguments.add(pyArgument.toString());
        }

        return arguments;
    }

    private static List<Step> createSteps(PyObject pySteps, String filePath) {
        List<Step> steps = new ArrayList<>();

        for (PyObject pyStep : pySteps.asIterable()){
            String name = null;
            try {
                name = getStringValue(pyStep, "name");
            } catch (MissingAttributeException e) {
                continue;
            }
            List<String> arguments = createArguments(pyStep.__findattr__("args"));

            steps.add(new Step(filePath, name, arguments));
        }

        return steps;
    }

    protected static String getStringValue(PyObject pyObject, String attribute) throws MissingAttributeException {
        PyObject pythonAttribute = pyObject.__findattr__(attribute);

        if(pythonAttribute == null){
            throw new MissingAttributeException();
        }

        return pythonAttribute.toString();
    }

    protected static List<String> getStringListValue(PyObject pyObject) {
        return getStringListValue(pyObject, "");
    }

    protected static List<String> getStringListValue(PyObject pyObject, String attribute) {
        List<String> list = new ArrayList<>();

        for (PyObject pyItem : pyObject.asIterable()){
            String value = attribute.length() == 0 ? pyItem.toString() : pyItem.__findattr__(attribute).toString();
            list.add(value);
        }

        return list;
    }

    protected static void loadResources(Project project, Settings settings, String directory) {
        ResourcesTable resourcesTable = settings.getResources();

        for(Resources resources : resourcesTable) {
            if(resources.getType() != Resources.Type.Resource) {
                continue;
            }

            String resourcePath = directory + File.separator + resources.getName();

            if(project.hasFile(resourcePath)){
                resources.setFile(project.getFile(resourcePath));
            }
            else{
                TestCaseFile resourcesFile = ResourcesFileFactory.create(project, resourcePath);
                resources.setFile(resourcesFile);
            }
        }
    }
}
