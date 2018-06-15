package lu.uni.serval.robotframework.model;

import java.util.ArrayList;
import java.util.List;

public class Variable {
    private String  name;
    private List<Argument> definition;

    public Variable() {
        definition = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addDefinitionElement(String element) {
        this.definition.add(new Argument(element));
    }

    public String getName() {
        return name;
    }

    public List<Argument> getDefinition() {
        return definition;
    }
}
