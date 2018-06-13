package lu.uni.serval.robotframework.model;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserKeyword extends KeywordDefinition implements Iterable<Step> {

    private String file;
    private List<Step> steps;
    private List<String> tags;

    public UserKeyword(String file, String name, List<String> arguments, String documentation, List<Step> steps, List<String> tags) {
        super(name, arguments, documentation);

        this.file = file;
        this.steps = steps;
        this.tags = tags;
    }

    public UserKeyword(Step step) {
        this(step.getFile(), step.getName(), step.getArguments(), "", new ArrayList<>(), new ArrayList<>());
    }

    public String getFile() {
        return file;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public List<String> getTags() {
        return tags;
    }

    @Nonnull
    public Iterator<Step> iterator() {
        return steps.iterator();
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
