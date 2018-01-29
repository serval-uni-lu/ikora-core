package lu.uni.serval.robotframework.model;

import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestCaseFileFactory {
    protected PyObject testCaseFileClass;
    protected String file;

    public TestCaseFileFactory() {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec( "from robot.api import TestCaseFile");

        this.testCaseFileClass = interpreter.get("TestCaseFile");
    }

    public TestCaseFile create(String filePath) {
        this.file = filePath;
        PyObject testCaseFileObject = testCaseFileClass.__call__(new PyString(""), new PyString(this.file));
        testCaseFileObject.__findattr__("populate").__call__();

        String directory = getStringValue(testCaseFileObject, "directory");
        String name = getStringValue(testCaseFileObject, "name");
        Settings settings = createSettingsTable(testCaseFileObject.__findattr__("setting_table"));
        TestCaseTable testCaseTable = createTestCaseTable(testCaseFileObject);
        KeywordTable keywordTable = createKeywordTable(testCaseFileObject.__findattr__("keyword_table"));
        VariableTable variableTable = createVariableTable(testCaseFileObject.__findattr__("variable_table"));

        loadResources(settings);

        return new TestCaseFile(directory, name, settings, testCaseTable, keywordTable, variableTable);
    }

    private TestCaseTable createTestCaseTable(PyObject testCaseFileObject) {
        TestCaseTable testCaseTable = new TestCaseTable();

        PyObject pyTestCaseTable = testCaseFileObject.__findattr__("testcase_table");

        for (PyObject pyTestCase : pyTestCaseTable.asIterable()){
            testCaseTable.add(createTestCase(pyTestCase));
        }

        return testCaseTable;
    }

    private TestCase createTestCase(PyObject pyTestCase){
        String name = getStringValue(pyTestCase, "name");
        String documentation = getStringValue(pyTestCase, "doc");
        List<Step> steps = createSteps(pyTestCase.__findattr__("steps"));

        return new TestCase(this.file, name, documentation, steps);
    }

    protected VariableTable createVariableTable(PyObject pyVariableTable) {

        return new VariableTable();
    }

    protected Settings createSettingsTable(PyObject pySettings) {
        ResourcesTable resourcesTable = createImportTable(pySettings.__findattr__("imports"));

        return new Settings(resourcesTable);
    }

    protected ResourcesTable createImportTable(PyObject pyImports) {
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

    private Resources.Type getResourceType(PyObject pyResource) {
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

    protected KeywordTable createKeywordTable(PyObject pyKeywordTable){
        KeywordTable keywordTable = new KeywordTable();

        for (PyObject pyUserKeyword : pyKeywordTable.asIterable()){
            keywordTable.add(createUserKeyword(pyUserKeyword));
        }

        return keywordTable;
    }

    private UserKeyword createUserKeyword(PyObject pyUserKeyword){
        String name = getStringValue(pyUserKeyword, "name");
        String documentation = getStringValue(pyUserKeyword, "doc");
        List<String> arguments = createArguments(pyUserKeyword.__findattr__("args"));
        List<Step> steps = createSteps(pyUserKeyword.__findattr__("steps"));

        return new UserKeyword(this.file, name, arguments, documentation, steps);
    }

    private List<String> createArguments(PyObject pyArguments) {
        List<String> arguments = new ArrayList<String>();

        for (PyObject pyArgument : pyArguments.asIterable()){
            arguments.add(pyArgument.toString());
        }

        return arguments;
    }

    private List<Step> createSteps(PyObject pySteps) {
        List<Step> steps = new ArrayList<Step>();

        for (PyObject pyStep : pySteps.asIterable()){
            String name = getStringValue(pyStep, "name");
            List<String> arguments = createArguments(pyStep.__findattr__("args"));

            steps.add(new Step(this.file, name, arguments));
        }

        return steps;
    }

    protected String getStringValue(PyObject pyObject, String attribute) {
        return pyObject.__findattr__(attribute).toString();
    }

    protected List<String> getStringListValue(PyObject pyObject, String attribute) {
        List<String> list = new ArrayList<String>();

        for (PyObject pyItem : pyObject.asIterable()){
            String value = pyItem.__findattr__(attribute).toString();
            list.add(value);
        }

        return list;
    }

    protected void loadResources(Settings settings) {
        ResourcesTable resourcesTable = settings.getResources();

        for(Resources resources : resourcesTable) {
            if(resources.getType() != Resources.Type.Resource) {
                continue;
            }

            File file = new File(this.file);

            ResourcesFileFactory factory = new ResourcesFileFactory();
            TestCaseFile resourcesFile = factory.create(file.getParent() + File.separator + resources.getName());

            resources.setFile(resourcesFile);
        }
    }
}
