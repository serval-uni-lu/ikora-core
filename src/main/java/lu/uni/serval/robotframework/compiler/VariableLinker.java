package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.*;
import org.apache.log4j.Logger;

import java.util.List;

public class VariableLinker {
    final static Logger logger = Logger.getLogger(VariableLinker.class);

    static public void link(Project project) throws Exception {
        for (TestCaseFile testCaseFile: project.getTestCaseFiles()) {

            for(UserKeyword userKeyword: testCaseFile.getElements(UserKeyword.class)) {
                linkSteps(userKeyword, testCaseFile);
            }
        }
    }

    static private void linkSteps(UserKeyword userKeyword, TestCaseFile testCaseFile) throws Exception {
        for(Step step: userKeyword) {
            for(Argument argument: step.getParameters()) {
                resolveArgument(argument, testCaseFile, userKeyword);
            }
        }
    }

    static private void resolveArgument(Argument argument, TestCaseFile testCaseFile, UserKeyword userKeyword) throws Exception {
        List<String> variables = argument.findVariables();

        for(String name: variables){
            Variable variable = userKeyword.findLocalVariable(name);

            if(variable != null){
                argument.setVariable(name, variable);
            }
            else {
                variable = testCaseFile.findVariable(name);

                if(variable == null) {
                    logger.error("Variable for argument \"" + name + "\" not found!");
                }
                else{
                    argument.setVariable(name, variable);
                }
            }
        }
    }
}
