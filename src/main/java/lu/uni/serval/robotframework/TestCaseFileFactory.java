package lu.uni.serval.robotframework;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class TestCaseFileFactory {
    private PyObject testCaseFileClass;

    public TestCaseFileFactory() {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("from robot.api import TestCaseFile");
        testCaseFileClass = interpreter.get("TestCaseFile");
    }

    public TestCaseFile create() {
        PyObject testCaseFileObject = testCaseFileClass.__call__();
        //testCaseFileObject.__findattr__("populate").__call__();

        return (TestCaseFile)testCaseFileObject.__tojava__(TestCaseFile.class);
    }
}
