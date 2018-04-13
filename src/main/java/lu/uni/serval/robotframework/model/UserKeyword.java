package lu.uni.serval.robotframework.model;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserKeyword implements Iterable<Step> {
    private List<Argument> arguments;
    private String file;
    private Argument name;
    private String documentation;
    private List<Step> steps;

    public UserKeyword(String file, String name, List<String> arguments, String documentation, List<Step> steps) {
        this.file = file;
        this.name = new Argument(name);
        this.documentation = documentation;
        this.steps = steps;

        this.arguments = new ArrayList<>();
        for(String argument : arguments) {
            this.arguments.add(new Argument(argument));
        }
    }

    public UserKeyword(Step step) {
        this(step.getFile(), step.getName(), step.getArguments(), "", new ArrayList<>());
    }

    public String getFile() {
        return file;
    }

    public Argument getName() {
        return name;
    }

    public String getDocumentation() {
        return documentation;
    }

    public List<Step> getSteps() {
        return steps;
    }

    @Nonnull
    public Iterator<Step> iterator() {
        return steps.iterator();
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public boolean isEqual(Object other) {
        if(other instanceof UserKeyword){
            return ((UserKeyword)other).file.equals(this.file)
                    && ((UserKeyword)other).name == this.name
                    && ((UserKeyword)other).steps == this.steps;

        }
        else if(other instanceof Step){
            String stepName = ((Step)other).getCleanName();
            stepName = stepName.replaceAll("\"[^\"]+\"", "");

            String keyword = this.getName().toString().trim();
            keyword = keyword.replaceAll("\"[^\"]+\"", "");

            return keyword.equalsIgnoreCase(stepName);
        }

        return super.equals(other);
    }
}
