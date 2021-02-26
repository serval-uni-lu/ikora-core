package lu.uni.serval.ikora.core.types;

public class StringType extends BaseType {
    public StringType(String name) {
        super(name, null);
    }

    public StringType(String name, String defaultValue) {
        super(name, defaultValue);
    }
}
