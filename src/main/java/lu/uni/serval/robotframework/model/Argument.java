package lu.uni.serval.robotframework.model;

public class Argument {
    private String value;

    Argument(String value) {
        this.value = value;
    }

    public boolean isVariable() {
        return value.matches("^\\$\\{.*?\\}$");
    }

    @Override
    public String toString() {
        return this.value;
    }
}
