package lu.uni.serval.robotframework.model;

import java.util.ArrayList;
import java.util.List;

public class UserKeyword extends TestCase {
    private List<String> arguments;

    public UserKeyword(String file, String name, List<String> arguments,
                       String documentation, List<Step> steps) {
        super(file, name, documentation, steps);

        this.arguments = arguments;
    }

    public UserKeyword(Step step) {
        this(step.getFile(), step.getName(), step.getArguments(), "", new ArrayList<Step>());
    }

    public List<String> getArguments() {
        return arguments;
    }

    public boolean isEqual(Step step) {
        String stepName = step.getName().trim().toLowerCase();
        stepName = stepName.replaceAll("^(given|when|then) ", "").trim();

        if (this.getName().trim().equalsIgnoreCase(stepName)) {
            if(this.getArguments().size() == step.getArguments().size()){
                return true;
            }
        }

        return false;
    }
}
