package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.exception.MissingAttributeException;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

public class ResourcesFileFactory extends ProjectFactory {
    private static PyObject resourceFileClass;

    static {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("from robot.api import ResourceFile");

        resourceFileClass = interpreter.get("ResourceFile");
    }

    public static TestCaseFile create(Project project, String filePath) {
        PyObject testCaseFileObject = resourceFileClass.__call__(new PyString(filePath));
        testCaseFileObject.__findattr__("populate").__call__();

        try {
            String directory = getStringValue(testCaseFileObject, "directory");
            String name = getStringValue(testCaseFileObject, "name");
            Settings settings = createSettingsTable(testCaseFileObject.__findattr__("setting_table"));
            KeywordTable keywordTable = createKeywordTable(testCaseFileObject.__findattr__("keyword_table"), filePath);
            VariableTable variableTable = createVariableTable(testCaseFileObject.__findattr__("variable_table"));

            loadResources(project, settings, directory);

            return new TestCaseFile(directory, filePath, name, settings, null, keywordTable, variableTable);
        } catch (MissingAttributeException e) {
            e.printStackTrace();
        }

        return null;
    }
}
