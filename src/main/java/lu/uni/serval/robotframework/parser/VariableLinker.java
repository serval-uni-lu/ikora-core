package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VariableLinker {
    static public void link(Project project) throws Exception {
        for (TestCaseFile testCaseFile: project.getTestCaseFiles()) {

            for(UserKeyword userKeyword: testCaseFile.getUserKeywords()) {
                linkSteps(userKeyword, testCaseFile);
            }
        }
    }

    static void linkSteps(UserKeyword userKeyword, TestCaseFile testCaseFile) throws Exception {
        List<String> arguments = userKeyword.getArguments();
        Set<String> localArguments = new HashSet<>(arguments);

        for(Step step: userKeyword) {
            for(Argument argument: step.getArguments()) {
                resolveArgument(argument, testCaseFile, localArguments);
            }
        }
    }

    static void resolveArgument(Argument argument, TestCaseFile testCaseFile, Set<String> localVariables) throws Exception {
        List<String> variables = argument.findVariables();

        for(String name: variables){
            if(localVariables.contains(name)){
                argument.setVariable(name, null);
                continue;
            }

            Variable value = testCaseFile.findVariable(name);

            if(value == null) {
                throw new Exception();
            }

            argument.setVariable(name, value);
        }
    }
}
