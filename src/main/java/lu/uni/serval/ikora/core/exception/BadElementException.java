package lu.uni.serval.ikora.core.exception;

public class BadElementException extends Exception {
    private Class<?> expected;
    private Class<?> observed;

    public BadElementException(Class<?> expected, Class<?> observed){
        this.expected = expected;
        this.observed = observed;
    }

    @Override
    public String getMessage(){
        return String.format("Expected element of type '%s' but got '%s' instead!",
                expected.getName(),
                observed.getName());
    }
}
