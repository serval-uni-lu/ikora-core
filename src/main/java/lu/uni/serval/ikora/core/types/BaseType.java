package lu.uni.serval.ikora.core.types;

public class BaseType {
    private final String name;
    private final String defaultValue;

    public BaseType(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean hasDefaultValue(){
        return defaultValue != null;
    }
}
