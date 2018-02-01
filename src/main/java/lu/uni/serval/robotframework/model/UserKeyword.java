package lu.uni.serval.robotframework.model;

import java.util.ArrayList;
import java.util.List;

public class UserKeyword extends TestCase {
    private List<Argument> arguments;

    public UserKeyword(String file, String name, List<String> arguments,
                       String documentation, List<Step> steps) {
        super(file, name, documentation, steps);

        this.arguments = new ArrayList<Argument>();
        for(String argument : arguments) {
            this.arguments.add(new Argument(argument));
        }
    }

    public UserKeyword(Step step) {
        this(step.getFile(), step.getName(), step.getArguments(), "", new ArrayList<Step>());
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public boolean isEqual(Step step) {
        String stepName = step.getName().trim().toLowerCase();
        stepName = stepName.replaceAll("^(given|when|then) ", "").trim();
        stepName = stepName.replaceAll("\"[^\"]+\"", "");

        String keyword = this.getName().toString().trim();
        keyword = keyword.replaceAll("\"[^\"]+\"", "");

        return keyword.equalsIgnoreCase(stepName);
    }
}
