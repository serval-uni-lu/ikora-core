package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.*;

import java.util.List;

public class VariableLinker {
    static public void link(Project project) throws Exception {
        for (TestCaseFile testCaseFile: project.getTestCaseFiles()) {

            for(UserKeyword userKeyword: testCaseFile.getUserKeywords()) {
                linkSteps(userKeyword, testCaseFile);
            }
        }
    }

    static void linkSteps(UserKeyword userKeyword, TestCaseFile testCaseFile) throws Exception {
        for(Step step: userKeyword) {
            for(Argument argument: step.getParameters()) {
                resolveArgument(argument, testCaseFile, userKeyword);
            }
        }
    }

    static void resolveArgument(Argument argument, TestCaseFile testCaseFile, UserKeyword userKeyword) throws Exception {
        List<String> variables = argument.findVariables();

        for(String name: variables){
            Variable variable = userKeyword.findLocalVariable(name);

            if(variable != null){
                argument.setVariable(name, variable);
            }
            else {
                variable = testCaseFile.findVariable(name);

                if(variable == null) {
                    throw new Exception();
                }

                argument.setVariable(name, variable);
            }
        }
    }
}
