package lu.uni.serval.robotframework.report;

public class ExecutionResult {
    public enum Type {
        Assert, Execute, Unknown, Aborted
    }

    public enum Error {
        None, WrongNumberArgument, AssertFailed, ElementNotFound,
        IllegalAccess, InvocationTarget, Implementation
    }

    private Type type;
    private Error error;
    private String message;

    public ExecutionResult(Type type) {
        this.type = type;
        this.error = Error.None;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isError() {
        return this.error != Error.None;
    }

    public boolean isAborted() {
        return this.type == Type.Aborted;
    }

    public boolean isExecution() {
        return this.type == Type.Execute;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public void setWrongNumberArugmentError(int expect, int got) {
        this.error = Error.WrongNumberArgument;
        this.message = "Expected at least " + expect + " argument" + (expect > 1 ? "s" : "") + " got " + got + " instead!";
    }

    public void setAssertionFailedError(String message) {
        this.error = Error.AssertFailed;
        this.message = message;
    }

    public void setElementNotFoundError(String locator) {
        this.error = Error.ElementNotFound;
        this.message = "DifferentiableString '" + locator + "' was not found";
    }
}
