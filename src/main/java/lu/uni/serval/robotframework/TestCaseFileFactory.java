package lu.uni.serval.robotframework;

import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestCaseFileFactory {
    public enum FileType {
        TestCaseFile, ResourceFile
    }

    private PyObject testCaseFileClass;
    private FileType fileType;

    public TestCaseFileFactory(FileType fileType) {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("from robot.api import TestCaseFile");

        this.fileType = fileType;
        this.testCaseFileClass = interpreter.get(fileType == FileType.TestCaseFile ? "TestCaseFile" : "ResourceFile");
    }

    public TestCaseFile create(String filePath) {
        PyObject testCaseFileObject = testCaseFileClass.__call__(new PyString(""), new PyString(filePath));
        testCaseFileObject.__findattr__("populate").__call__();

        String directory = getStringValue(testCaseFileObject, "directory");
        String name = getStringValue(testCaseFileObject, "name");
        Settings settings = createSettingsTable(testCaseFileObject.__findattr__("setting_table"));
        TestCaseTable testCaseTable = createTestCaseTable(testCaseFileObject);
        KeywordTable keywordTable = createKeywordTable(testCaseFileObject.__findattr__("keyword_table"));
        VariableTable variableTable = createVariableTable(testCaseFileObject.__findattr__("variable_table"));

        loadResources(directory, settings);

        return new TestCaseFile(directory, name, settings, testCaseTable, keywordTable, variableTable);
    }

    private TestCaseTable createTestCaseTable(PyObject testCaseFileObject) {
        TestCaseTable testCaseTable = new TestCaseTable();

        if(fileType == FileType.TestCaseFile) {
            PyObject pyTestCaseTable = testCaseFileObject.__findattr__("testcase_table");

            for (PyObject pyTestCase : pyTestCaseTable.asIterable()){
                testCaseTable.add(createTestCase(pyTestCase));
            }
        }

        return testCaseTable;
    }

    private TestCase createTestCase(PyObject pyTestCase){
        String name = getStringValue(pyTestCase, "name");
        String documentation = getStringValue(pyTestCase, "doc");
        List<Step> steps = createSteps(pyTestCase.__findattr__("steps"));

        return new TestCase(name, documentation, steps);
    }

    private VariableTable createVariableTable(PyObject pyVariableTable) {

        return new VariableTable();
    }

    private Settings createSettingsTable(PyObject pySettings) {
        ResourcesTable resourcesTable = createImportTable(pySettings.__findattr__("imports"));

        return new Settings(resourcesTable);
    }

    private ResourcesTable createImportTable(PyObject pyImports) {
        ResourcesTable resourcesTable = new ResourcesTable();

        for (PyObject pyResource : pyImports.__findattr__("data").asIterable()) {
            String name = getStringValue(pyResource, "name");
            List<String> arguments = getStringListValue(pyResource.__findattr__("args"), "args");
            String comment = getStringValue(pyResource,"comment");

            resourcesTable.add(new Resources(name, arguments, comment));
        }

        return resourcesTable;
    }

    private KeywordTable createKeywordTable(PyObject pyKeywordTable){
        KeywordTable keywordTable = new KeywordTable();

        for (PyObject pyUserKeyword : pyKeywordTable.asIterable()){
            keywordTable.add(createUserKeyword(pyUserKeyword));
        }

        return keywordTable;
    }

    private UserKeyword createUserKeyword(PyObject pyUserKeyword){
        String name = getStringValue(pyUserKeyword, "name");
        String documentation = getStringValue(pyUserKeyword, "doc");
        List<String> arguments = getStringListValue(pyUserKeyword.__findattr__("args"), "name");
        List<Step> steps = createSteps(pyUserKeyword.__findattr__("steps"));

        return new UserKeyword(name, arguments, documentation, steps);
    }

    private List<Step> createSteps(PyObject pySteps) {
        List<Step> steps = new ArrayList<Step>();

        for (PyObject pyStep : pySteps.asIterable()){
            String name = getStringValue(pyStep, "name");

            List<String> arguments = new ArrayList<String>();
            for (PyObject pyArgument : pyStep.__findattr__("args").asIterable()){
                arguments.add(pyArgument.toString());
            }

            steps.add(new Step(name, arguments));
        }

        return steps;
    }

    private String getStringValue(PyObject pyObject, String attribute) {
        return pyObject.__findattr__(attribute).toString();
    }

    private List<String> getStringListValue(PyObject pyObject, String attribute) {
        List<String> list = new ArrayList<String>();

        for (PyObject pyItem : pyObject.asIterable()){
            String value = pyItem.__findattr__(attribute).toString();
            list.add(value);
        }

        return list;
    }

    private void loadResources(String directory, Settings settings) {
        ResourcesTable resourcesTable = settings.getResources();

        for(Resources resources : resourcesTable) {
            TestCaseFileFactory factory = new TestCaseFileFactory(FileType.ResourceFile);
            TestCaseFile resourcesFile = factory.create(directory + File.separator + resources.getName());

            resources.setResourcesFile(resourcesFile);
        }
    }
}
